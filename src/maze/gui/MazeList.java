package maze.gui;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import maze.Main;
import maze.model.MazeInfo;

public class MazeList extends JScrollPane implements ListSelectionListener
{
   /**
    * Internal JList instance.
    */
   private final JList myList = new JList();
   /**
    * The maze view we are attached to.
    */
   private final MazeView mazeView;
   /**
    * Holds a global list selection model that is shared among all instances of
    * this class. This allows all the currently selected mazes to be changed at
    * once.
    */
   private static ListSelectionModel listSelectionModel;

   /**
    * Getter that initializes on first call.
    */
   private static ListSelectionModel getListSelectionModel()
   {
      if (listSelectionModel == null)
      {
         listSelectionModel = new DefaultListSelectionModel();
         listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      }
      return listSelectionModel;
   }

   /**
    * Sole Constructor.
    * @param mazeView The MazeView this MazeList is to be attached to.
    */
   public MazeList(MazeView mazeView)
   {
      this.mazeView = mazeView;
      super.setBorder(BorderFactory.createTitledBorder("Available Mazes"));
      super.setViewportView(this.myList);
      this.myList.setModel(Main.getPrimaryFrameInstance().getMazeInfoModel().getMazeInfoComboBoxModel());
      this.myList.setSelectionModel(getListSelectionModel());
      this.myList.getSelectionModel().addListSelectionListener(this);
      /**
       * This uses the save renderer for all lists. If it is later decided that
       * only the editor list should display dirty status this can be changed to
       * an option.
       */
      this.myList.setCellRenderer(new OpenMazeRenderer());
      if (this.myList.getModel().getSize() > 0 && this.myList.isSelectionEmpty())
      {
         this.myList.setSelectedIndex(0);
      }
      //Set the model of the maze view to the currently selected.
      this.valueChanged(null);
   }

   /**
    * Get the underlying JList instance.
    */
   public JList getList()
   {
      return this.myList;
   }

   /**
    * This event is triggered when the list selection changes.
    */
   @Override
   public void valueChanged(ListSelectionEvent e)
   {
      try
      {
         this.mazeView.setModel( ((MazeInfo) this.myList.getSelectedValue()).getModel());
      }
      catch (RuntimeException ex)
      {
         ex.printStackTrace();
      }
   }

   /**
    * Renders a MazeInfo object displaying its dirty status.
    */
   private static class OpenMazeRenderer extends DefaultListCellRenderer
   {
      @Override
      public Component getListCellRendererComponent(JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus)
      {
         MazeInfo mi = (MazeInfo) value;
         final String postfix;
         if (mi.isDirty())
            postfix = "*";
         else
            postfix = "";
         Component c = super.getListCellRendererComponent(list,
                                                          mi.getName() + postfix,
                                                          index,
                                                          isSelected,
                                                          cellHasFocus);
         JComponent jc = (JComponent) c;
         jc.setToolTipText(mi.getPath());
         return jc;
      }
   }

}
