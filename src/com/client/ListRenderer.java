package com.client;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.plaf.metal.MetalIconFactory;

public class ListRenderer extends DefaultListCellRenderer {
    private Map<Object, Icon> icons = null;

    public ListRenderer(Map<Object, Icon> icons) {
        this.icons = icons;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Icon icon = icons.get(value);
        
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);      
        label.setIcon(icon);
        
        return label;
    }

    /*public static void main(String[] args) {
        // setup mappings for which icon to use for each value
        Map<Object, Icon> icons = new HashMap<Object, Icon>();
        
        icons.put("Alberto", MetalIconFactory.getTreeComputerIcon());
        icons.put("Cocorin", MetalIconFactory.getTreeComputerIcon());
        icons.put("Invitado", MetalIconFactory.getTreeComputerIcon());
        
        Vector<Object> vector = new Vector<>();
        
        for(Map.Entry<Object, Icon> entry : icons.entrySet())
            vector.add(entry.getKey());

        JFrame frame = new JFrame("Icon List");
        
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // create a list with some test data
        JList list = new JList(vector);

        // create a cell renderer to add the appropriate icon
        list.setCellRenderer(new ListRenderer(icons));
        
        frame.add(list);
        frame.pack();
        frame.setVisible(true);
    }*/
}