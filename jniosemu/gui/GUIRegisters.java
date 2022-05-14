package jniosemu.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import jniosemu.emulator.register.*;
import jniosemu.events.*;

/** Creates and manages the GUI component of the register view in the emulator. */
public class GUIRegisters extends JPanel implements EventObserver, ListSelectionListener {

  /** Reference to EventManager used to receive and send events. */
  private transient EventManager eventManager;

  /** List component used to display register values. */
  private JList registerList;

  /**
   * Initiates the creation of GUI components and adds itself to the Event Manager as an observer.
   *
   * @post eventManager reference is set for this object.
   * @calledby GUIManager.setup()
   * @calls setup(), EventManager.addEventObserver()
   * @param eventManager The Event Manager object.
   */
  public GUIRegisters(EventManager eventManager) {
    super();

    this.eventManager = eventManager;

    setup();

    // add events to listen to
    EventManager.EVENT[] events = {
      EventManager.EVENT.REGISTER_CHANGE,
      EventManager.EVENT.COMPILER_COMPILE,
      EventManager.EVENT.EMULATOR_CLEAR
    };
    this.eventManager.addEventObserver(events, this);
  }

  /**
   * Setup GUI components and attributes.
   *
   * @post components created and added to panel
   * @calledby GUIRegisters
   */
  private void setup() {
    this.setPreferredSize(new Dimension(150, 0));

    // registers
    registerList = new JList();
    registerList.setBackground(Color.WHITE);
    registerList.setFont(new Font("Monospaced", Font.PLAIN, 11));
    registerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    registerList.setCellRenderer(
        new RegisterCellRenderer(registerList.getFontMetrics(registerList.getFont())));
    registerList.addListSelectionListener(this);

    MouseListener mouseListener =
        new MouseAdapter() {
          /** Clear selection when selecting an already selected register. */
          public void mouseClicked(MouseEvent e) {
            if (registerList.isSelectionEmpty()) return;

            if (e.getClickCount() == 2) {
              if (registerList.locationToIndex(e.getPoint()) == registerList.getSelectedIndex()) {
                registerList.clearSelection();
              }
            }
          }
        };
    registerList.addMouseListener(mouseListener);

    // scrollbars
    JScrollPane scrollPane = new JScrollPane(registerList);

    // put everything into the emulator panel
    this.setLayout(new BorderLayout());
    this.add(scrollPane, BorderLayout.CENTER);
  }

  /**
   * Set new vector of registers.
   *
   * @param registers Register vector to set.
   */
  public void setRegisters(Vector<Register> registers) {
    if (registers == null) {
      // clear registers view
      registerList.setModel(new DefaultListModel());
      return;
    }

    // get currently selected register (if any)
    int index = registerList.getSelectedIndex();

    registerList.setListData(registers);

    // (re)set selected register
    if (index != -1) registerList.setSelectedIndex(index);
  }

  /** Called whenever currently selected register changes. */
  public void valueChanged(ListSelectionEvent e) {
    if (e.getValueIsAdjusting() == false) {
      int index = registerList.getSelectedIndex();

      if (index != -1) {
        // register selected
        Register reg = (Register) registerList.getModel().getElementAt(index);

        eventManager.sendEvent(EventManager.EVENT.REGISTER_VIEW_SELECT, reg);
      }
    }
  }

  public void update(EventManager.EVENT eventIdentifier, Object obj) {
    switch (eventIdentifier) {
      case REGISTER_CHANGE:
        setRegisters((Vector<Register>) obj);
        break;
      case COMPILER_COMPILE:
        registerList.clearSelection();
        break;
      case EMULATOR_CLEAR:
        setRegisters(null);
        break;
    }
  }

  /** Custom cell renderer for the JList in the register view. */
  class RegisterCellRenderer extends JPanel implements ListCellRenderer {

    private Register regObj;

    private final FontMetrics metrics;
    private final int baseline;
    private final int width;
    private final int height;

    public RegisterCellRenderer(FontMetrics metrics) {
      super();
      setOpaque(true);
      setFont(registerList.getFont());

      this.baseline = metrics.getAscent();
      this.height = metrics.getHeight();
      this.width = registerList.getWidth();
      this.metrics = metrics;
    }

    /** Return the renderers fixed size. */
    public Dimension getPreferredSize() {
      return new Dimension(width, height);
    }

    /** Cell rendered methos sets background/foreground color and stores Register object. */
    public Component getListCellRendererComponent(
        JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      this.regObj = (Register) value;

      if (isSelected) {
        switch (this.regObj.getState()) {
          case READ:
            setBackground(GUIManager.HIGHLIGHT_SELECTED_GREEN);
            break;
          case WRITE:
            setBackground(GUIManager.HIGHLIGHT_SELECTED_RED);
            break;
          default:
            setBackground(list.getSelectionBackground());
        }
      } else {
        switch (this.regObj.getState()) {
          case READ:
            setBackground(GUIManager.HIGHLIGHT_GREEN);
            break;
          case WRITE:
            setBackground(GUIManager.HIGHLIGHT_RED);
            break;
          default:
            setBackground(list.getBackground());
        }
      }

      return this;
    }

    /** Custom paint method bypassing standard JComponent painting to optimize performance. */
    public void paintComponent(Graphics g) {
      // clear background
      g.setColor(getBackground());
      g.fillRect(0, 0, getWidth(), getHeight());

      // draw not supported registers in another color
      if (this.regObj.getState() == Register.STATE.DISABLED) g.setColor(GUIManager.FONT_DISABLED);
      else g.setColor(Color.black);

      // register name
      g.drawString(this.regObj.getName(), 2, this.baseline);

      // register value
      String tmp = this.regObj.getValueAsString();
      g.drawString(tmp, getWidth() - this.metrics.stringWidth(tmp) - 2, this.baseline);
    }
  }
}
