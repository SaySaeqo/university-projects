using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace lab2
{
    /// <summary>
    /// Logika interakcji dla klasy Window1.xaml
    /// </summary>
    public partial class Window1 : Window
    {
        public bool WasClickedOk = false;
        public FileAttributes Attributes = new FileAttributes();
        private TreeViewItem parentObject;
        public Window1(TreeViewItem twi)
        {
            InitializeComponent();
            parentObject = twi;
        }

        void OnClickOk(object sender, RoutedEventArgs e)
        {
            if (Attributes.HasFlag(FileAttributes.Directory))
            {
                const string pattern = "^[a-zA-Z0-9_~-]{1,8}$";
                if (!Regex.IsMatch(FileNameBox.Text, pattern))
                {
                    ErrorMessage.Text = "Niepoprawna nazwa folderu.";
                    return;
                }
            }
            else
            {
                const string pattern = "^[a-zA-Z0-9_~-]{1,8}\\.(html|php|txt)$";
                if (!Regex.IsMatch(FileNameBox.Text, pattern))
                {
                    ErrorMessage.Text = "Niepoprawna nazwa pliku \n" +
                        "(dozwolone rozszerzenia: txt,php,html).";
                    return;
                }
            }
            for (int i = 0; i < parentObject.Items.Count; i++)
            {
                if (((TreeViewItem)parentObject.Items.GetItemAt(i)).Header.ToString() == FileNameBox.Text)
                {
                    ErrorMessage.Text = "Plik/folder o tej nazwie już istnieje!";
                    return;
                }
            }
            WasClickedOk = true;
            this.Close();
        }

        void OnClickCancel(object sender, RoutedEventArgs e)
        {
            this.Close();
        }

        void HandleChecked(object sender, RoutedEventArgs e)
        {
            switch (sender)
            {
                case RadioButton rb:
                    switch (rb.Content)
                    {
                        case "File":
                            Attributes &= ~FileAttributes.Directory;
                            break;
                        case "Directory":
                            Attributes |= FileAttributes.Directory;
                            break;
                    }
                break;

                case CheckBox cb:
                    switch (cb.Content)
                    {
                        case "Readonly":
                            Attributes |= FileAttributes.ReadOnly;
                            break;
                        case "Archive":
                            Attributes |= FileAttributes.Archive;
                            break;
                        case "Hidden":
                            Attributes |= FileAttributes.Hidden;
                            break;
                        case "System":
                            Attributes |= FileAttributes.System;
                            break;
                    }
                    break;
            }
        }
        void HandleUnchecked(object sender, RoutedEventArgs e)
        {
            CheckBox cb = (CheckBox)sender;
            switch (cb.Content)
            {
                case "Readonly":
                    Attributes &= ~FileAttributes.ReadOnly;
                    break;
                case "Archive":
                    Attributes &= ~FileAttributes.Archive;
                    break;
                case "Hidden":
                    Attributes &= ~FileAttributes.Hidden;
                    break;
                case "System":
                    Attributes &= ~FileAttributes.System;
                    break;
            }
        }
    }
}