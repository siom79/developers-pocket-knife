package developers.pocket.knife.ui.tools.classfinder;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import developers.pocket.knife.config.ConfigurationFactory;
import developers.pocket.knife.exceptions.TechnicalException;
import developers.pocket.knife.i18n.Messages;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class ClassFinderUI extends JPanel {
    @Inject
    Messages messages;
    @Inject
    JTextField inputDirectory;
    @Inject
    JButton buttonDirectory;
    @Inject
    JTextField inputClassName;
    @Inject
    JButton buttonSearch;
    @Inject
    JButton buttonStop;
    @Inject
    private JTable outputTable;
    @Inject
    private JCheckBox checkBoxCaseSensitive;
    @Inject
    private JCheckBox checkBoxContains;
    @Inject
    private JLabel outputLocation;
    @Inject
    @ConfigurationFactory.ConfigurationValue(key = ConfigurationFactory.ConfigurationKey.DefaultDirectory)
    private String defaultDirectory;
    ClassFinderUIModel model;
    private List<SearchResult> searchResults = new LinkedList<>();

    @PostConstruct
    public void postConstruct() {
        buildComponents();
    }

    private void buildComponents() {
        final JPanel panelRef = this;
        final AbstractTableModel tableModel = new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return searchResults.size();
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                SearchResult searchResult = searchResults.get(rowIndex);
                switch (columnIndex) {
                    case 0:
                        return searchResult.getLocation();
                    case 1:
                        return searchResult.getZipFilePath();
                }
                return "n.a";
            }

            @Override
            public String getColumnName(int column) {
                switch (column) {
                    case 0:
                        return messages.location();
                    case 1:
                        return messages.zipFilePath();
                }
                return "n.a";
            }
        };
        buttonDirectory.setText(messages.choose() + "...");
        buttonDirectory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentInputDir = inputDirectory.getText();
                if(currentInputDir != null && currentInputDir.trim().length() > 0) {
                    File curInputDirFile = new File(currentInputDir);
                    if(curInputDirFile.exists()) {
                        defaultDirectory = currentInputDir;
                    }
                }
                JFileChooser fc = new JFileChooser(new File(defaultDirectory));
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int showOpenDialog = fc.showOpenDialog(getRootPane());
                if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fc.getSelectedFile();
                    inputDirectory.setText(selectedFile.getAbsolutePath());
                    defaultDirectory = selectedFile.getAbsolutePath();
                }
            }
        });
        inputClassName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch(panelRef, tableModel);
            }
        });
        checkBoxCaseSensitive.setText(messages.caseSensitive());
        checkBoxContains.setText(messages.contains());
        buttonSearch.setText(messages.search());
        buttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch(panelRef, tableModel);
            }
        });
        buttonStop.setText(messages.stop());
        buttonStop.setEnabled(false);
        buttonStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model != null) {
                    model.cancel(true);
                }
                buttonStop.setEnabled(false);
                buttonSearch.setEnabled(true);
                outputLocation.setText("");
            }
        });
        outputTable.setModel(tableModel);
        outputTable.setPreferredScrollableViewportSize(outputTable.getPreferredSize());
        outputTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
    }

    private void performSearch(JPanel panelRef, final AbstractTableModel tableModel) {
        final String inputDirectoryText = inputDirectory.getText();
        if (inputDirectoryText.trim().length() == 0) {
            JOptionPane.showMessageDialog(panelRef,
                    messages.noDirectory(),
                    messages.error(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        File directoryFile = new File(inputDirectoryText);
        if (!directoryFile.exists()) {
            JOptionPane.showMessageDialog(panelRef,
                    messages.fileDoesNotExist(directoryFile.getAbsolutePath()),
                    messages.error(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        final String inputClassNameText = inputClassName.getText();
        if (inputClassNameText.trim().length() == 0) {
            JOptionPane.showMessageDialog(panelRef,
                    messages.noClassName(),
                    messages.error(),
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            searchResults.clear();
            tableModel.fireTableDataChanged();
            buttonStop.setEnabled(true);
            buttonSearch.setEnabled(false);
            boolean caseSensitive = checkBoxCaseSensitive.isSelected();
            boolean contains = checkBoxContains.isSelected();
            model = new ClassFinderUIModel(inputDirectoryText, inputClassNameText, caseSensitive, contains) {
                @Override
                protected void done() {
                    buttonStop.setEnabled(false);
                    buttonSearch.setEnabled(true);
                    outputLocation.setText("");
                }

                @Override
                protected void process(List<List<SearchResult>> chunks) {
                    for (List<SearchResult> chunk : chunks) {
                        searchResults.addAll(chunk);
                    }
                    tableModel.fireTableDataChanged();
                }
            };
            model.execute();
        } catch (Exception e1) {
            throw new TechnicalException(TechnicalException.Reason.InternalError);
        }
    }

    public JPanel buildUI() {
        FormLayout formLayout = new FormLayout("right:p, 3dlu, fill:p:grow, 3dlu, p", "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, fill:p:grow, 3dlu, p");

        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(formLayout);
        builder.setDefaultDialogBorder();

        builder.add(new JLabel(messages.directory() + ":"), cc.xy(1, 1));
        builder.add(inputDirectory, cc.xy(3, 1));
        builder.add(buttonDirectory, cc.xy(5, 1));

        builder.add(new JLabel(messages.fileName() + ":"), cc.xy(1, 3));
        builder.add(inputClassName, cc.xyw(3, 3, 3));

        builder.add(buildSearchCriteriaPanel(), cc.xyw(3, 5, 3));
        builder.add(buildButtonPanel(), cc.xyw(1, 7, 5));

        builder.add(new JScrollPane(outputTable), cc.xyw(1, 9, 5));

        builder.add(outputLocation, cc.xyw(1, 11, 5));

        return builder.getPanel();
    }

    public JPanel buildButtonPanel() {
        FormLayout formLayout = new FormLayout("p, 3dlu, p", "p");

        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(formLayout);

        builder.add(buttonSearch, cc.xy(1, 1));
        builder.add(buttonStop, cc.xy(3, 1));

        return builder.getPanel();
    }

    public JPanel buildSearchCriteriaPanel() {
        FormLayout formLayout = new FormLayout("p, 3dlu, p", "p");

        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(formLayout);

        builder.add(checkBoxCaseSensitive, cc.xy(1, 1));
        builder.add(checkBoxContains, cc.xy(3, 1));

        return builder.getPanel();
    }
}
