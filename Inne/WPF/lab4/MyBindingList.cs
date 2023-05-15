using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace lab4
{


    class MyBindingList<T> : BindingList<T>
    {
        public MyBindingList(ObservableCollection<T> cars) : base(cars)
        {
        }

        private bool isSorted = false;

        protected override bool IsSortedCore => isSorted;

        protected override bool SupportsSortingCore => true;
        protected override bool SupportsSearchingCore => true;



        protected override void ApplySortCore
            (System.ComponentModel.PropertyDescriptor prop, System.ComponentModel.ListSortDirection direction)
        {
            if (prop.PropertyType.GetInterface("IComparable") != null)
            {
                IOrderedEnumerable<T> ordered;
                if (direction == ListSortDirection.Descending)
                    ordered = from item in Items
                              orderby prop.GetValue(item) descending
                              select item;
                else
                    ordered = from item in Items
                              orderby prop.GetValue(item)
                              select item;

                List<T> tmpItems = ordered.ToList();

                Items.Clear();
                for (int i = 0; i < tmpItems.Count; i++)
                    Items.Add(tmpItems[i]);

                isSorted = true;
                OnListChanged(new ListChangedEventArgs(ListChangedType.Reset, -1));
            }
        }

        public void Sort(string property, ListSortDirection direction)
        {
            PropertyDescriptorCollection properties = TypeDescriptor.GetProperties(typeof(T));
            PropertyDescriptor prop = properties.Find(property, true);
            if (prop != null)
            {
                ApplySortCore(prop, direction);
            }
        }

        protected override int FindCore
            (System.ComponentModel.PropertyDescriptor prop, object key)
        {
            for (int i = 0; i < Items.Count; i++)
                if (prop.GetValue(Items[i]).Equals(key))
                    return i;
            
            return -1;
        }

        public int Find(string property, object key)
        {
            PropertyDescriptorCollection properties = TypeDescriptor.GetProperties(typeof(T));
            PropertyDescriptor prop = properties.Find(property, true);
            if (prop != null)
            {
                return FindCore(prop, key);
            }
            return -1;
        }

        public override string ToString()
        {
            string str = "";
            for (int i = 0; i < Items.Count; i++)
            {
                str += Items[i].ToString() + "\n";
            }
            return str;
        }
    }
}
