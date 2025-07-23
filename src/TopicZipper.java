import javax.swing.*;
import java.awt.*;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class TopicZipper {

    // Assuming topicPath is accessible here (e.g., as a static field or passed in)

    public static void zipTopicWithProgress(String topicID, Component parentComponent,boolean startingTopic) {
    	String path = Constants.topicPath+File.separator+topicID;
    	Path sourceDir = Paths.get(path);
        String zipFilePath = path+".zip";
        // Create and show the progress dialog immediately on the EDT
        JDialog progressDialog = new JDialog((Window) SwingUtilities.getWindowAncestor(parentComponent), "Zipping Topic...", Dialog.ModalityType.APPLICATION_MODAL);
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        JButton cancelButton = new JButton("Cancel");

        JLabel messageLabel = new JLabel("Starting to zip '" + path + "'...");

        JPanel dialogPanel = new JPanel(new BorderLayout(10, 10));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        dialogPanel.add(messageLabel, BorderLayout.NORTH);
        dialogPanel.add(progressBar, BorderLayout.CENTER);
        dialogPanel.add(cancelButton,BorderLayout.SOUTH);
        

        progressDialog.setContentPane(dialogPanel);
        progressDialog.pack();
        progressDialog.setLocationRelativeTo(parentComponent); // Center dialog relative to parent
        progressDialog.setResizable(false);
        progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Prevent closing during zipping

        // Use SwingWorker for the background task
        SwingWorker<Boolean, String> worker = new SwingWorker<>() {
            private long totalBytesToZip = 0; // Total size of files to zip
            private final AtomicLong bytesZipped = new AtomicLong(0); // Bytes processed so far

            @Override
            protected Boolean doInBackground() throws Exception {
                // Step 1: Calculate total size (optional but good for accurate progress)
                // This might also take time for very large directories, consider if you need it
                publish("Calculating total size...");
                totalBytesToZip = Files.walk(sourceDir)
                                     .filter(Files::isRegularFile)
                                     .filter(path -> !sourceDir.relativize(path).toString().replace("\\", "/").equals("related.nfo"))
                                     .mapToLong(path -> {
                                         try {
                                             return Files.size(path);
                                         } catch (IOException e) {
                                             // Handle inaccessible files gracefully, maybe log and count as 0
                                             System.err.println("Could not get size of " + path + ": " + e.getMessage());
                                             return 0;
                                         }
                                     })
                                     .sum();

                if (totalBytesToZip == 0) {
                    publish("No files to zip or calculation failed. Creating empty zip if directory exists.");
                }

                // Step 2: Perform the zipping
                try (ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFilePath)), StandardCharsets.UTF_8)) {
                	zipOut.setLevel(Deflater.BEST_SPEED);
                	Files.walk(sourceDir).forEach(path -> {
                        if (isCancelled()) { // Check for cancellation
                            publish("Zipping cancelled.");
                            return; // Stop processing
                        }
                        try {
                            String entryName = sourceDir.relativize(path).toString().replace("\\", "/");
                            if (entryName.equalsIgnoreCase("related.nfo")) {
                                return;
                            }
                            ZipEntry zipEntry = new ZipEntry(entryName + (Files.isDirectory(path) ? "/" : ""));
                            zipOut.putNextEntry(zipEntry);

                            if (!Files.isDirectory(path)) {
                                long fileSize = Files.size(path);
                                Files.copy(path, zipOut);
                                bytesZipped.addAndGet(fileSize); // Increment bytes zipped

                                // Publish progress
                                int progress = (totalBytesToZip > 0) ? (int) ((bytesZipped.get() * 100) / totalBytesToZip) : 0;
                                // publish a message and progress value.
                                // The message will be handled by process(), the progress by setProgress()
                                publish(String.format("Zipping... (%s of %s)", Constants.getHRFileSize(bytesZipped.get()), Constants.getHRFileSize(totalBytesToZip)));
                                setProgress(progress);
                            }
                            zipOut.closeEntry();
                        } catch (IOException e) {
                            // If an individual file fails, log it but try to continue
                            publish("Error zipping " + path.getFileName() + ": " + e.getMessage());
                            System.err.println("Error zipping " + path + ": " + e.getMessage());
                            // Do not re-throw as RuntimeException, it will stop the worker
                        }
                    });
                }
                return true; // Zipping completed successfully
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                // This method runs on the EDT
                // Update the message label with the latest message from publish()
                if (!chunks.isEmpty()) {
                    messageLabel.setText(chunks.get(chunks.size() - 1)); // Get the latest message
                }
            }

            @Override
            protected void done() {
                // This method runs on the EDT after doInBackground() completes or cancels
                try {
                    Boolean result = get(); // Get the result or re-throw any exception from doInBackground
                    if (result != null && result) {
                    	Topic topic = Constants.getTopic(topicID);
                    	topic.setHash();
                    	topic.setPublished(true);
                    	if (startingTopic)
                    		topic.reShow();
                		else
                    		JOptionPane.showMessageDialog(parentComponent, "'" + topic.getTitle() + "' successfully zipped. Generating Hash...", "Zipping Complete", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(parentComponent, "Zipping '" + path + "' failed or was cancelled.", "Zipping Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (java.util.concurrent.CancellationException e) {
                    JOptionPane.showMessageDialog(parentComponent, "Zipping of '" + path + "' was cancelled.", "Zipping Cancelled", JOptionPane.INFORMATION_MESSAGE);
                    // Clean up partially created zip file if desired
                    try {
                        Files.deleteIfExists(Paths.get(zipFilePath));
                    } catch (IOException ex) {
                        System.err.println("Failed to delete partially created zip file: " + ex.getMessage());
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(parentComponent, "An error occurred during zipping: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                } finally {
                    progressDialog.dispose(); // Close the progress dialog
                }
            }
        };
        cancelButton.addActionListener(e -> {
            worker.cancel(true);
        });
        // Add a PropertyChangeListener to update the JProgressBar
        worker.addPropertyChangeListener(evt -> {
            if ("progress".equals(evt.getPropertyName())) {
                int progress = (Integer) evt.getNewValue();
                progressBar.setValue(progress);
            }
        });
        
        // Start the SwingWorker on a background thread
        worker.execute();

        // Show the dialog (this will block the EDT until the dialog is closed)
        // The dialog will be closed by the SwingWorker's done() method
        SwingUtilities.invokeLater(() -> progressDialog.setVisible(true));
    }

  
}