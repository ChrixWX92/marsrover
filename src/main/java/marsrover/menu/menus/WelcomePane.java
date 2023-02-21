package marsrover.menu.menus;

import marsrover.Main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class WelcomePane extends JFrame implements ActionListener {

    JLabel intro;
    JLabel prompt, xLabel, zLabel, roverXLabel, roverZLabel, facingLabel;
    JTextField xBox, zBox, roverXBox, roverZBox;
    JRadioButton northButton, eastButton, southButton, westButton;
    ButtonGroup buttonGroup;
    JButton button;
    Container container;

    DocumentFilter intFilter = (new DocumentFilter(){
        final Pattern regEx = Pattern.compile("\\d*");
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            Matcher matcher = regEx.matcher(text);
            if(!matcher.matches()){
                return;
            }
            super.replace(fb, offset, length, text, attrs);
        }
    });

    public WelcomePane() {
        intro = new JLabel("\uD83D\uDE80 Welcome to the Mars Rover program! \uD83D\uDE80");
        intro.setFont(new Font("Courier", Font.BOLD, 20));
        prompt = new JLabel("Please enter your preferred grid size and initial rover location.");
        xLabel = new JLabel("Size X:");
        xBox = new JTextField();
        zLabel = new JLabel("Size Z:");
        zBox = new JTextField();
        roverXLabel = new JLabel("Rover X:");
        roverXBox = new JTextField();
        roverZLabel = new JLabel("Rover Z:");
        roverZBox = new JTextField();
        roverZLabel = new JLabel("Rover Z:");
        roverZBox = new JTextField();
        facingLabel = new JLabel("Rover Facing:");
        northButton = new JRadioButton("North", true);
        eastButton = new JRadioButton("East");
        southButton = new JRadioButton("South");
        westButton = new JRadioButton("West");
        buttonGroup = new ButtonGroup();
        buttonGroup.add(northButton);
        buttonGroup.add(eastButton);
        buttonGroup.add(southButton);
        buttonGroup.add(westButton);
        button = new JButton("Generate Plateau");
        container = getContentPane();
        container.setLayout(null);
        setBounds();
        setFilter();
        addComponents();
        addActionListener();
    }

    public void setBounds() {
        intro.setBounds(85, 10, 600, 30);
        prompt.setBounds(112, 60, 600, 30);
        xLabel.setBounds(120, 110, 100, 30);
        xBox.setBounds(200, 110, 250, 30);
        zLabel.setBounds(120, 160, 100, 30);
        zBox.setBounds(200, 160, 250, 30);
        roverXLabel.setBounds(120, 210, 100, 30);
        roverXBox.setBounds(200, 210, 250, 30);
        roverZLabel.setBounds(120, 260, 100, 30);
        roverZBox.setBounds(200, 260, 250, 30);
        facingLabel.setBounds(55, 310, 100, 30);
        northButton.setBounds(170, 310, 100, 30);
        eastButton.setBounds(270, 310, 100, 30);
        southButton.setBounds(370, 310, 100, 30);
        westButton.setBounds(470, 310, 100, 30);
        button.setBounds(165, 370, 250, 30);
    }

    public void setFilter() {
        ((AbstractDocument) xBox.getDocument()).setDocumentFilter(intFilter);
        ((AbstractDocument) zBox.getDocument()).setDocumentFilter(intFilter);
        ((AbstractDocument) roverXBox.getDocument()).setDocumentFilter(intFilter);
        ((AbstractDocument) roverZBox.getDocument()).setDocumentFilter(intFilter);
    }

    public void addComponents() {
        container.add(intro);
        container.add(prompt);
        container.add(xLabel);
        container.add(xBox);
        container.add(zLabel);
        container.add(zBox);
        container.add(roverXLabel);
        container.add(roverXBox);
        container.add(roverZLabel);
        container.add(roverZBox);
        container.add(facingLabel);
        container.add(northButton);
        container.add(eastButton);
        container.add(southButton);
        container.add(westButton);
        container.add(button);
    }

    public void addActionListener() {
        button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == button) {

            int x = xBox.getText().length() > 0 ? Integer.parseInt(xBox.getText()) : 5;
            int z = zBox.getText().length() > 0 ? Integer.parseInt(zBox.getText()) : 5;
            int roverX = roverXBox.getText().length() > 0 ? Integer.parseInt(roverXBox.getText()) : 0;
            int roverZ = roverZBox.getText().length() > 0 ? Integer.parseInt(roverZBox.getText()) : 0;

            char facing = 0;
            if (northButton.isSelected()) facing = 'N';
            if (eastButton.isSelected()) facing = 'E';
            if (southButton.isSelected()) facing = 'S';
            if (westButton.isSelected()) facing = 'W';
            char finalFacing = facing;
            
            Main.taskQueue.add(() -> Main.stage.engageControl(x+1, z+1, roverX, roverZ, finalFacing));

            this.dispose();
        }

    }

}