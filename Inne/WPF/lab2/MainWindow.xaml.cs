using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace lab2
{
    /// <summary>
    /// Logika interakcji dla klasy MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
            Tree.SelectedItemChanged += RashUpdate;
        }

        void OnClickExit(object sender, RoutedEventArgs e)
        {
            System.Windows.Application.Current.Shutdown();
        }

        void OnClickOpen(object sender, RoutedEventArgs e)
        {
            var dlg = new System.Windows.Forms.FolderBrowserDialog() { Description = "Select directory to open" };
            dlg.ShowDialog();
            var path = dlg.SelectedPath;
            // DEBUG
            Debug.Text = path;
            // END OF DEBUG

            FileTreeViewItem root = new FileTreeViewItem(path);
            Tree.Items.Add(root);

        }

        void RashUpdate(object sender, RoutedEventArgs e)
        {
            TreeViewItem selected = (TreeViewItem)Tree.SelectedItem;
            if (selected == null) return;

            FileAttributes attributes;
            try
            {
                attributes = File.GetAttributes(selected.Tag.ToString());
            }
            catch (Exception ex)
            {
                Debug.Text += "\n" + ex.Message;
                return;
            }

            char[] code = { '-', '-', '-', '-', '\0' };
            if (attributes.HasFlag(FileAttributes.ReadOnly))
                code[0] = 'r';
            if (attributes.HasFlag(FileAttributes.Archive))
                code[1] = 'a';
            if (attributes.HasFlag(FileAttributes.System))
                code[2] = 's';
            if (attributes.HasFlag(FileAttributes.Hidden))
                code[3] = 'h';
            

            StringBuilder sb = new StringBuilder();

            using (StringWriter sw = new StringWriter(sb))
            {
                sw.Write(code);
            }

            status.Text = sb.ToString();
        }

    }
}
