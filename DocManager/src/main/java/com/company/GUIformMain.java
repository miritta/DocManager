package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIformMain extends JFrame  {


    private JButton bWorker;
    private JTextField tPathWorker;
    private JButton bPattern;
    private JTextField tPathPattern;
    private JButton bSave;
    private JLabel DocEditor;
    private JPanel myPanel;
    private DocManager docManager = new DocManager();

    public GUIformMain() {

        bWorker.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int retValue = fileChooser.showOpenDialog(myPanel);
                System.out.println("bWorker is clicked");
                if(retValue == JFileChooser.APPROVE_OPTION) {
                    DocManager.excelWorkerFile = fileChooser.getSelectedFile();
                    System.out.println(DocManager.excelWorkerFile.getAbsolutePath());
                    docManager.showWorkers(docManager.readWorkerList(DocManager.excelWorkerFile.getAbsolutePath()));
                    System.out.println("file is chosen");
                } else {
                    System.out.println("file isn't chosen");
                }
            }
        });
        bPattern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int retValue = fileChooser.showOpenDialog(myPanel);
                System.out.println("bPattern is clicked");
                if(retValue == JFileChooser.APPROVE_OPTION) {
                    DocManager.docPatternFile = fileChooser.getSelectedFile();
                    System.out.println("file is chosen");
                } else {
                    System.out.println("file isn't chosen");
                }
            }
        });
        bSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int retValue = fileChooser.showSaveDialog(myPanel);
                System.out.println("bSave is clicked");
                if(retValue == JFileChooser.APPROVE_OPTION) {
                    DocManager.docResults = fileChooser.getSelectedFile();
                    System.out.println("file is chosen");
                    if(DocManager.excelWorkerFile != null && DocManager.docPatternFile != null && DocManager
                            .docResults != null) {
                        docManager.changePattern(DocManager.docPatternFile.getAbsolutePath(),
                                DocManager.docResults.getAbsolutePath(), docManager.readWorkerList(DocManager.excelWorkerFile
                                        .getAbsolutePath()));
                    }
                }else {
                    System.out.println("file isn't chosen");
                }
            }
        });
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("GUIformMain");
        frame.setContentPane(new GUIformMain().myPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
