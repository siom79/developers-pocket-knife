package developers.pocket.knife.ui.tools.validatexml;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import developers.pocket.knife.i18n.Messages;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class ValidateXmlUI extends JPanel {
    @Inject
    private ValidateXmlUIModel model;
    @Inject
    private Messages messages;
    @Inject
    private JTextField inputXmlFile;
    @Inject
    private JButton buttonXmlFile;
    @Inject
    private JTextField inputSchemaFile;
    @Inject
    private JButton buttonSchemaFile;
    @Inject
    private JButton buttonValidate;
    @Inject
    private JTable outputTable;

    @PostConstruct
    public void postConstruct() {
        buildComponents();
    }

    private void buildComponents() {
        final JPanel panelRef = this;
        final AbstractTableModel tableModel = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return model.getParserErrors().size();
            }

            @Override
            public int getColumnCount() {
                return 4;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                List<ValidateXmlUIModel.ParserError> parserErrors = model.getParserErrors();
                ValidateXmlUIModel.ParserError parserError = parserErrors.get(rowIndex);
                switch (columnIndex) {
                    case 0:
                        return parserError.getType();
                    case 1:
                        return parserError.getLine();
                    case 2:
                        return parserError.getColumn();
                    case 3:
                        return parserError.getMessage();
                }
                return "n.a";
            }

            @Override
            public String getColumnName(int column) {
                switch (column) {
                    case 0:
                        return messages.type();
                    case 1:
                        return messages.line();
                    case 2:
                        return messages.column();
                    case 3:
                        return messages.message();
                }
                return "n.a";
            }
        };
        outputTable.setModel(tableModel);
        outputTable.setPreferredScrollableViewportSize(outputTable.getPreferredSize());
        outputTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        outputTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        outputTable.getColumnModel().getColumn(2).setPreferredWidth(50);
        outputTable.getColumnModel().getColumn(3).setPreferredWidth(400);
        outputTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        buttonXmlFile.setText(messages.choose() + "...");
        buttonXmlFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int showOpenDialog = fc.showOpenDialog(getRootPane());
                if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fc.getSelectedFile();
                    model.setXmlFile(selectedFile);
                    inputXmlFile.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        buttonSchemaFile.setText(messages.choose()+"...");
        buttonSchemaFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int showOpenDialog = fc.showOpenDialog(getRootPane());
                if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fc.getSelectedFile();
                    model.setSchemaFile(selectedFile);
                    inputSchemaFile.setText(selectedFile.getAbsolutePath());
                }
            }
        });
        buttonValidate.setText(messages.validate());
        buttonValidate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputXmlFileText = inputXmlFile.getText();
                if(inputXmlFileText.trim().length() == 0) {
                    JOptionPane.showMessageDialog(panelRef,
                            messages.noXmlFile(),
                            messages.error(),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String inputSchemaFileText = inputSchemaFile.getText();
                if(inputSchemaFileText.trim().length() == 0) {
                    JOptionPane.showMessageDialog(panelRef,
                            messages.noSchemaFile(),
                            messages.error(),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                File xmlFile = new File(inputXmlFileText);
                if(!xmlFile.exists()) {
                    JOptionPane.showMessageDialog(panelRef,
                            messages.fileDoesNotExist(xmlFile.getAbsolutePath()),
                            messages.error(),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                File schemaFile = new File(inputSchemaFileText);
                if(!schemaFile.exists()) {
                    JOptionPane.showMessageDialog(panelRef,
                            messages.fileDoesNotExist(schemaFile.getAbsolutePath()),
                            messages.error(),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                model.setXmlFile(xmlFile);
                model.setSchemaFile(schemaFile);
                model.validate();
                tableModel.fireTableDataChanged();
                outputTable.repaint();
                if(model.getParserErrors().size() == 0) {
                    JOptionPane.showMessageDialog(panelRef,
                            messages.validationSuccessful(),
                            messages.information(),
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }
        });
    }

    public JPanel buildUi() {
        FormLayout formLayout = new FormLayout("right:p, 3dlu, fill:p:grow, 3dlu, p", "p, 3dlu, p, 3dlu, p, 3dlu, fill:p:grow");

        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(formLayout);
        builder.setDefaultDialogBorder();

        builder.add(new JLabel(messages.xmlFile() + ":"), cc.xy(1, 1));
        builder.add(inputXmlFile, cc.xy(3, 1));
        builder.add(buttonXmlFile, cc.xy(5, 1));

        builder.add(new JLabel(messages.schemaFile() + ":"), cc.xy(1, 3));
        builder.add(inputSchemaFile, cc.xy(3, 3));
        builder.add(buttonSchemaFile, cc.xy(5, 3));

        builder.add(buttonValidate, cc.xy(1, 5));

        JScrollPane scrollPane = new JScrollPane(outputTable);
        builder.add(scrollPane, cc.xyw(1, 7, 5));

        return builder.getPanel();
    }
}
