using FastMember;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Forms;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace lab4
{
    /// <summary>
    /// Logika interakcji dla klasy MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {

        WindowViewModel _viewModel;

        public MainWindow()
        {
            InitializeComponent();

            _viewModel = (WindowViewModel)DataContext;

            LinqQuery();
            NoLambdaChallenge();
            MyBindingListExample();

        }

        private void MyBindingListExample()
        {
            var mbl = new MyBindingList<Car>(_viewModel.Cars);
            ShowMB(mbl);
            mbl.Sort("motor", ListSortDirection.Descending);
            ShowMB(mbl);
            int result = mbl.Find("year", 2009);
            if (result != -1)
                ShowMB(mbl[result]);
            else
                ShowMB("Nie znaleziono.");

        }

        private void NoLambdaChallenge()
        {
            Func<Car, Car, int> arg1 = byHorsePower;
            Predicate<Car> arg2 = IsTDI;
            Action<Car> arg3 = ShowMB;
            List<Car> myCars = new List<Car>(_viewModel.Cars);

            myCars.Sort(new Comparison<Car>(arg1));
            myCars.FindAll(arg2).ForEach(arg3);
        }

        private void ShowMB(object obj)
        {
            System.Windows.MessageBox.Show(obj.ToString());
        }

        private bool IsTDI(Car obj)
        {
            return obj.motor.model == "TDI";
        }

        private int byHorsePower(Car x, Car y)
        {
            return x.CompareTo(y);
        }

        private void LinqQuery()
        {
            {
                var elements = _viewModel.Cars.Where(car => car.model == "A6")
                   .Select(car => new
                   {
                       engineType = car.motor.model == "TDI" ? "diesel" : "petrol",
                       hppl = car.motor.horsePower / car.motor.displacement
                   })
                   .GroupBy(a => a.engineType)
                   .Select(a => new
                   {
                       engineType = a.Key,
                       avgHPPL = a.Average(b => b.hppl)
                   })
                   .OrderByDescending(a => a.avgHPPL)
                   .Select(a => a);
                foreach (var e in elements)
                    Console.WriteLine(e.engineType + ": " + e.avgHPPL);
            }
            {
                var elements = from car in _viewModel.Cars
                               where car.model == "A6"
                               group car by car.motor.model == "TDI"
                               into g
                               orderby g.Average(car => car.motor.horsePower / car.motor.displacement) descending
                               select new
                               {
                                   engineType = g.Key ? "diesel" : "petrol",
                                   avgHPPL = g.Average(car => car.motor.horsePower / car.motor.displacement)
                               };
                foreach (var e in elements)
                    Console.WriteLine(e.engineType + ": " + e.avgHPPL);
            }

        }

        public void OnClickFind(object sender, RoutedEventArgs e)
        {
            object result = null;
            switch ((searchInBox.SelectedItem as ComboBoxItem).Content)
            {
                case "Car Model":
                    foreach (var item in DG.Items)
                    {
                        Car car = item as Car;
                        if (car.model == searchForBox.Text)
                        {
                            result = car;
                            break;
                        }
                    }
                    break;
                case "Model":
                    foreach (var item in DG.Items)
                    {
                        Car car = item as Car;
                        if (car.motor.model == searchForBox.Text)
                        {
                            result = car;
                            break;
                        }
                    }
                    break;
                case "Displacement":
                    {
                        Double number;
                        try
                        {
                            number = Double.Parse(searchForBox.Text);
                        }
                        catch (FormatException)
                        {
                            FormatErrorHandle();
                            return;
                        }
                        foreach (var item in DG.Items)
                        {
                            Car car = item as Car;
                            if (car.motor.displacement == number)
                            {
                                result = car;
                                break;
                            }
                        }
                    }
                    break;
                case "Horse Power":
                    {
                        Double number;
                        try
                        {
                            number = Double.Parse(searchForBox.Text);
                        }
                        catch (FormatException)
                        {
                            FormatErrorHandle();
                            return;
                        }
                        foreach (var item in DG.Items)
                        {
                            Car car = item as Car;
                            if (car.motor.horsePower == number)
                            {
                                result = car;
                                break;
                            }
                        }
                    }
                    break;
                default: // "Year":
                    {
                        int number;
                        try
                        {
                            number = Int32.Parse(searchForBox.Text);
                        }
                        catch (FormatException)
                        {
                            FormatErrorHandle();
                            return;
                        }
                        foreach (var item in DG.Items)
                        {
                            Car car = item as Car;
                            if (car.year == number)
                            {
                                result = car;
                                break;
                            }
                        }
                    }
                    break;
            }
            if (result != null)
            {
                DG.SelectedItems.Clear();
                DG.SelectedItems.Add(result);
                DG.ScrollIntoView(result);

                DataGridRow row = (DataGridRow)DG.ItemContainerGenerator.ContainerFromItem(result);
                row.MoveFocus(new TraversalRequest(FocusNavigationDirection.Next));
            }
        }

        void FormatErrorHandle()
        {
            ;
        }

        public void OnKeyDownHandle(object sender, System.Windows.Input.KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
            {
                e.Handled = true;
                OnClickFind(sender, e);
            }
                
        }
    }
}
