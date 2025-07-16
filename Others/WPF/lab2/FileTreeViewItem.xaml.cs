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
    /// Logika interakcji dla klasy FileTreeViewItem.xaml
    /// </summary>
    public partial class FileTreeViewItem : TreeViewItem
    {
        public FileTreeViewItem(string path)
        {
            InitializeComponent();
            // ustal swoje dzieci
            bool is_dir = File.GetAttributes(path).HasFlag(System.IO.FileAttributes.Directory);
            if (is_dir)
            {
                var files = from file in Directory.EnumerateFiles(path, "*", SearchOption.TopDirectoryOnly)
                            select new FileTreeViewItem(file);
                var dirs = from dir in Directory.EnumerateDirectories(path, "*", SearchOption.TopDirectoryOnly)
                           select new FileTreeViewItem(dir);
                var items = files.Union(dirs);
                foreach (TreeViewItem item in items)
                {
                    this.Items.Add(item);
                }
            }
            // ustal siebie
            Header = path.Split('\\').Last();
            Tag = path;
            AddContextMenu(is_dir);
        }
        public FileTreeViewItem(FileTreeViewItem parent, string name, FileAttributes attributes)
        {
            InitializeComponent();

            bool is_dir = attributes.HasFlag(FileAttributes.Directory);
            string path = parent.Tag.ToString() + '\\' + name;

            // stwórz siebie na dysku
            if (is_dir)
                Directory.CreateDirectory(path);
            else
                File.Create(path);
            File.SetAttributes(path, attributes);

            // ustal siebie
            Header = name;
            Tag = path;
            AddContextMenu(is_dir);
        }

        private void AddContextMenu(bool is_directory)
        {
            ContextMenu cm = new ContextMenu();

            if (is_directory)
            {
                {
                    var mi = new MenuItem
                    {
                        Header = "Create",
                        Tag = this
                    };
                    mi.Click += OnClickCreate;
                    cm.Items.Add(mi);
                }
            }
            else
            {
                {
                    var mi = new MenuItem
                    {
                        Header = "Open",
                        Tag = this
                    };
                    mi.Click += OnClickOpen;
                    cm.Items.Add(mi);
                }
            }

            {
                var mi = new MenuItem
                {
                    Header = "Delete",
                    Tag = this
                };
                mi.Click += OnClickDelete;
                cm.Items.Add(mi);
            }

            this.ContextMenu = cm;
        }

        void OnClickOpen(object sender, RoutedEventArgs e)
        {
            var mi = (MenuItem)sender;
            var twi = (FileTreeViewItem)mi.Tag;
            MainWindow main_wnd = (MainWindow)Application.Current.MainWindow;
            try
            {
                FileStream fs = File.OpenRead(twi.Tag.ToString());
                StreamReader reader = new StreamReader(fs);
                main_wnd.Debug.Text = reader.ReadToEnd();
                reader.Close();
            }
            catch { }
        }

        void OnClickCreate(object sender, RoutedEventArgs e)
        {
            var mi_tmp = (MenuItem)sender;
            var twi = (FileTreeViewItem)mi_tmp.Tag;

            // DEBUG
            MainWindow main_wnd = (MainWindow)Application.Current.MainWindow;
            main_wnd.Debug.Text += "\n" + twi.Header + " Create!";
            // END OF DEBUG

            var dlg = new Window1(twi);
            dlg.ShowDialog();
            if (dlg.WasClickedOk)
            {
                FileTreeViewItem item;
                try
                {
                    item = new FileTreeViewItem(twi, dlg.FileNameBox.Text, dlg.Attributes);
                }
                catch 
                {
                    // DEBUG
                    main_wnd.Debug.Text += "\n" + twi.Header + " nie udało się Create :c";
                    // END OF DEBUG
                    return;
                }
                twi.Items.Add(item);
            }
        }

        private void RecursiveDirectoryDelete(string path)
        {

            FileAttributes attr = File.GetAttributes(path);
            bool is_dir = attr.HasFlag(FileAttributes.Directory);

            if (attr.HasFlag(FileAttributes.ReadOnly))
                File.SetAttributes(path, attr & ~FileAttributes.ReadOnly);

            if (is_dir)
            {
                var files = from file in Directory.EnumerateFiles(path, "*", SearchOption.TopDirectoryOnly)
                            select file;
                var dirs = from dir in Directory.EnumerateDirectories(path, "*", SearchOption.TopDirectoryOnly)
                           select dir;
                var items = files.Union(dirs);
                foreach (string item_path in items)
                {
                    RecursiveDirectoryDelete(item_path);
                }
                System.GC.Collect();
                System.GC.WaitForPendingFinalizers();
                Directory.Delete(path);
            }
            else
                File.Delete(path);
        }

        void OnClickDelete(object sender, RoutedEventArgs e)
        {
            var mi = (MenuItem)sender;
            var twi = (FileTreeViewItem)mi.Tag;

            // DEBUG
            MainWindow main_wnd = (MainWindow)Application.Current.MainWindow;
            main_wnd.Debug.Text += "\n" + twi.Header + " Delete!";
            // END OF DEBUG

            string path = twi.Tag.ToString();
            try
            {
                RecursiveDirectoryDelete(path);   
            }
            catch (Exception ex)
            {
                // DEBUG
                main_wnd.Debug.Text += "\n" + twi.Header + " nie udało się Delete :c\n";
                main_wnd.Debug.Text += ex.Message;
                // END OF DEBUG
                return;
            }

            ItemsControl parent = (ItemsControl)twi.Parent;
            parent.Items.Remove(twi);
        }

    }
}
