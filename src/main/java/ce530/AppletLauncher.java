/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ce530;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;

public class AppletLauncher {

    public static void main(String[] args) throws IOException {
        var applets = parseApplets();

        var listDisplay = applets.stream().map(info -> {
            return String.format("%s: %s", info.jarName.replace(".jar", ""), info.description);
        }).collect(Collectors.toList()).toArray(new String[]{});
        var list = new JList<>(listDisplay);
        var scroller = new JScrollPane(list);
        list.setSelectedIndex(0);
        var frame = new JFrame();

        ActionListener onLaunch = e -> {
            var appletInfo = applets.get(list.getSelectedIndex());
            try {
                frame.setVisible(false);
                frame.dispose();
                var appletFrame = new JFrame();
                appletFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                var applet = loadApplet(appletInfo.jarName);
                appletFrame.setLayout(new GridLayout(1, 1));

                var htmlPane = new JEditorPane();
                var scrollPane = new JScrollPane(htmlPane);
                htmlPane.setEditable(false);
                var kit = new HTMLEditorKit();
                htmlPane.setEditorKit(kit);
                htmlPane.setDocument(kit.createDefaultDocument());
                htmlPane.setText(appletInfo.pageHtml);

                var splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, applet, scrollPane);
                appletFrame.add(splitPane);
                appletFrame.setSize(1024, 1024);
//                appletFrame.getContentPane().add(applet);
                applet.init();
                applet.start();
                appletFrame.setVisible(true);
                splitPane.setDividerLocation(0.75);
            } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
                ex.printStackTrace();
            }
        };

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    onLaunch.actionPerformed(new ActionEvent(e.getSource(), e.getID(), ""));
                }
            }
        });

        list.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    onLaunch.actionPerformed(new ActionEvent(e.getSource(), e.getID(), ""));
                }
            }
        });
        frame.setLayout(new GridLayout(1, 1));
        frame.add(scroller);
        frame.setSize(1024, 1024);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static Applet loadApplet(String jarName) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        var jarPath = "/ce530/applets/" + jarName;
        // Read the jar file as a resource stream and collect each entry
        var inputStream = AppletLauncher.class.getResourceAsStream(jarPath);
        var jarStream = new JarInputStream(inputStream);
        var jarEntries = new ArrayList<JarEntry>();
        var entry = jarStream.getNextJarEntry();
        while (entry != null) {
            jarEntries.add(entry);
            entry = jarStream.getNextJarEntry();
        }
        // Search through each entry in the applet jar to find a .class file in the base directory
        // since it appears that all the applet classes are in the default package.
        var clsName = jarEntries.stream()
                .map(JarEntry::getName)
                .filter(name -> !name.contains("/") && !name.contains("$") && name.endsWith(".class"))
                .findFirst()
                .map(name -> name.replace(".class", ""))
                .orElseThrow();
        System.out.println("Found applet class " + clsName);

        // Stream the jar as a resource again and extract to a temporary file. It appears that there's no straightforward
        // way to have the URLClassLoader work with a url that is "inside" our application jar (aka zip file).
        inputStream = AppletLauncher.class.getResourceAsStream(jarPath);
        var tempFile = Files.createTempFile("", ".jar");
        tempFile.toFile().deleteOnExit();
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        var url = tempFile.toUri().toURL();
        System.out.println(url);
        var cl = new URLClassLoader(new URL[]{url}, AppletLauncher.class.getClassLoader());
        var cls = Class.forName(clsName, true, cl);
        return (Applet) cls.getConstructor().newInstance();
    }

    private static List<AppletInfo> parseApplets() throws IOException {
        var contents = new String(AppletLauncher.class.getResourceAsStream("/ce530/applets/applets.html").readAllBytes());
        var doc = Jsoup.parse(contents);
        var applets = new ArrayList<AppletInfo>();
        for (Element el : doc.getElementsByTag("li")) {
            var link = el.child(0);
            var desc = link.text();
            var appletPage = link.attr("href");
            var pageHtml = new String(AppletLauncher.class.getResourceAsStream("/ce530/applets/" + appletPage).readAllBytes());
            var appletDoc = Jsoup.parse(pageHtml);
            var appletTag = appletDoc.getElementsByTag("applet").first();
            var archiveStr = appletTag.attr("archive");
            var jarName = archiveStr.split(",")[0];
            appletTag.remove();
            applets.add(new AppletInfo(jarName, desc, appletDoc.outerHtml()));
        }

        return applets;
    }

    private static class AppletInfo {
        String jarName;
        String description;
        String pageHtml;

        public AppletInfo(String jarName, String description, String pageHtml) {
            this.jarName = jarName;
            this.description = description;
            this.pageHtml = pageHtml;
        }

        @Override
        public String toString() {
            return "AppletInfo{" +
                    "jarName='" + jarName + '\'' +
                    ", description='" + description + '\'' +
                    ", pageHtml='" + pageHtml + '\'' +
                    '}';
        }
    }
}

