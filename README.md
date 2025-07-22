 <html>
	<body style='font-family:sans-serif; color:#EEEEEE; background-color:#2E2E2E; padding:10px;'>
		<h2 style='color:#FF5555;'>🟥 Redlines</h2>
		<p><b>“No Thread Left Behind”</b> — A decentralized topic-sharing network.</p>
		<p>Redlines is a standalone Java-based application designed for privacy-respecting, node-to-node topic sharing.
		With a focus on resilience, transparency, and decentralized autonomy, Redlines enables users to distribute annotated
		topics complete with files, sources, and metadata — all without needing external servers.</p>
		<h3>🚀 Features</h3>
		<ul>
			<li><b>Decentralized Network:</b> Peer-to-peer architecture with topic propagation across trusted nodes.</li>
			<li><b>Fully Self-Contained:</b> Ships with a custom Java runtime — no external Java install required.</li>
			<li><b>Rich Topics:</b> Embed files (documents, videos, images, etc.), add annotations, and cite sources.</li>
			<li><b>Primary vs. Secondary Sources:</b> Differentiate between firsthand and analytical materials.</li>
			<li><b>Debug Console (Optional):</b> Toggle live debug output to monitor the system behind the scenes.</li>
			<li><b>Smart Port Handling:</b> Automatically sets up UPnP if available, or allows fallback to manual port forwarding.</li>
			<li><b>Cross-Platform:</b> Windows, Linux, macOS — run from a single AppImage or native executable.</li>
		</ul>
		<h3>📦 Installation</h3>
		<p><b>No Java? No problem.</b> Redlines includes a custom JDK-13 runtime.<br>
		Download it. Run it. That’s it.</p>
		<p><i>Note: On first launch, Redlines may ask to retry on a different port if the default is in use.</i></p>
		<h3>📁 Topic Structure</h3>
		<ul>
			<li>A <b>title</b> & <b>description</b></li>
			<li><b>sources</b> — Primary and secondary sources (URLs or files).</li>
			<li><b>annotations</b> — Comments, tags, and inline analysis.</li>
			<li><b>related topics</b> — References to other linked topics</li>
		</ul>
		<h3>🔒 Privacy Notes</h3>
		<ul>
			<li>Topics are authored, not owned — only the original author can publish updates.</li>
			<li>All connections are direct between nodes — no cloud storage or centralized index.</li>
			<li>Annotations are sandboxed per topic to preserve editorial integrity.</li>
		</ul>
		<h3>🛠️ Developer Notes</h3>
		<ul>
			<li>Built using Java 13, Swing UI, and JCEF (Chromium Embedded Framework).</li>
			<li>jlink and jpackage used to produce a self-contained distributable.</li>
			<li>JavaFX not required — all media/viewers are native, JCEF-based, or VLC-powered.</li>
			<li>Debug mode available via <i>Settings > Debug Output</i>.</li>
		</ul>
		<h3>🌐 Networking</h3>
		<ul>
			<li>Attempts UPnP port opening. Falls back to manual port forwarding.</li>
			<li>Nodes identified by CID — a hash derived from IP and port.</li>
			<li>"Go Public" mode toggles LAN-only vs. public visibility.</li>
		</ul>
		<h3>💡 Example Use Cases</h3>
		<ul>
			<li>Academic collaboration — share annotated primary sources.</li>
			<li>Open historical archives — preserve original documents with decentralized reach.</li>
			<li>Political transparency — source original footage with layered fact-checking.</li>
			<li>Personal research library — build and distribute portable topic folders.</li>
		</ul>
		<h3>💬 Support & Feedback</h3>
		<p>This project is in public testing. Bugs or feedback? Email: <b>axzxc99@gmail.com</b></p>
		<h3>📜 License</h3>
		<p>MIT License. Attribution appreciated but not required.</p>
	</body>
</html>
