🟥 Redlines
“No Thread Left Behind” — A decentralized topic-sharing network.

Redlines is a standalone Java-based application designed for privacy-respecting, node-to-node topic sharing. With a focus on resilience, transparency, and decentralized autonomy, Redlines enables users to distribute annotated topics complete with files, sources, and metadata — all without needing external servers.

🚀 Features
Decentralized Network: Peer-to-peer architecture with topic propagation across trusted nodes.

Fully Self-Contained: Ships with a custom Java runtime — no external Java install required.

Rich Topics: Embed files (documents, videos, images, etc.), add annotations, and cite sources.

Primary vs. Secondary Sources: Differentiate between firsthand and analytical materials.

Debug Console (Optional): Toggle live debug output to monitor the system behind the scenes.

Smart Port Handling: Automatically sets up UPnP if available, or allows fallback to manual port forwarding.

Cross-Platform: Works on Windows, Linux, and (likely) macOS — all from a single AppImage or native executable.

📦 Installation
💡 No Java? No problem. Redlines includes a custom JDK-13 runtime.

Download it. Run it. That’s it.

Note: On first launch, Redlines may ask to retry on a different port if the default is in use.

📁 Topic Structure
Each topic contains:

A title & description

sources — Primary and secondary sources (URLs or files).

annotations — Comments, tags, and inline analysis.

related topics — References to other topics you link with

🔒 Privacy Notes
Topics are authored, not owned — only the original author can publish updates.

All connections are direct between nodes — no cloud storage or centralized index.

Annotations are sandboxed per topic to preserve editorial integrity.

🛠️ Developer Notes
Built using Java 13, Swing UI, and JCEF (Chromium Embedded Framework).

jlink and jpackage used to produce a self-contained distributable.

JavaFX not required — all media and source viewers are native, JCEF-based, or prepacakged with VLC.

Debug mode available via Settings > Debug Output.

🌐 Networking
Redlines will attempt to:

Use UPnP to open its port automatically.

Fall back to manual port forwarding configuration if UPnP fails.

Nodes are identified by CID — a hashed ID derived from IP and port.

You can toggle "Go Public" mode to make your node accessible from outside your LAN.

💡 Example Use Cases
Academic collaboration — sharing annotated primary sources.

Open historical archives — preserving original documents with decentralized reach.

Political transparency — sourcing original footage with layered fact-checking.

Personal research library — build and distribute portable topic folders.

💬 Support & Feedback
This project is now in public testing. If you encounter any bugs, crashes, or have feature suggestions, please open an issue or email axzxc99@gmail.com.

📜 License
MIT License. Attribution appreciated but not required.