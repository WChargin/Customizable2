Customizable2
=============

Customizable is a simple-to-use and highly extensible property library for Swing applications.

Create properties like this:

    TextProperty name = new TextProperty("Name", ""); // second param is default value
    
`TextProperty` is an `AbstractProperty<String>`, so you can use:

 * `name.getValue()` &ndash; returns a `String`
 * `name.setValue(String)` &ndash; sets the value
    
Add them to a property set (convenience for `ArrayList<AbstractProperty<?>>`) like this:

    PropertySet ps = new PropertySet();
    ps.add(name);
    
Put them in a Swing app like this:

    PropertyPanel panel = new PropertyPanel(ps, true, false);
    // The parameters are:
    // Collection<? extends AbstractProperty<?>> properties: the properties to use
    // boolean editMode: whether the properties or editable or display-only
    // boolean changeableMode: whether the user can toggle edit mode
    
Listen for changes like this:

    name.addChangeListener(new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent ce) {
            // your code here
        }
    });

Make your own properties by extending `AbstractProperty<T>`. If you want them to be usable in a Swing application, extend `AbstractSwingProperty<T, E extends JComponent, V extends JComponent>` instead, where `E` and `V` are the types of your editor and viewer components. For example, `TrueFalseProperty` extends `AbstractSwingProperty<Boolean, JCheckBox, JLabel>`. You'll need to implement the following methods:

 * `E createEditor()` &ndash; creates a new editable view for the contents
 * `V createViewer()` &ndash; creates a new read-only view for the contents
 * `void updateEditor(E)` &ndash; updates the editor after changes are made
 * `void updateViewer(V)` &ndash; updates the viewer after changes are made
 
If there's no good way to edit your value in-place, you can use a `JButton` as the editor type and spawn a dialog to edit in. This is used in properties like `ColorProperty` and `FileProperty`.
 
Built-in properties include:

| Property class              | Type parameters                          |Notes                                                        |
|-----------------------------|------------------------------------------|-------------------------------------------------------------|
| `ColorProperty`             | `<java.awt.Color, JButton, ColorViewer>` | custom view component                                       |
| `CounterProperty`           | `<Integer, CounterEditor, JLabel>`       | editor can be either a spinner or a slider                  |
| `FileProperty`              | `<java.io.File, FileEditor, JLabel>`     | editor is a panel with "select" and "clear" buttons         |
| `LongTextProperty`          | `<String, JScrollPane, JScrollPane>`     | scroll panes have `JTextArea`s                              |
| `MessageProperty`           | `<String, JLabel, JLabel>`               | never editable                                              |
| `MultipleChoiceProperty<T>` | `<T, JComboBox, JLabel>`                 | works well with enums but also with any collection or array |
| `TextProperty`              | `<String, JTextField, JLabel>`           | vanilla                                                     |
| `TimeProperty`              | `<Time, TimePanel, JLabel>`              | has hours, minutes, seconds                                 |
| `TrueFalseProperty`         | `<Boolean, JCheckBox, JLabel>`           | label text for `true`/`false` set in constructor            |
