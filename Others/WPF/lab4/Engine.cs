using System;
using System.Collections.Generic;
using System.Text;
using System.Xml.Serialization;

namespace lab4
{
    public class Engine : IComparable
    {
        public double displacement { get; set; }
        public double horsePower { get; set; }
        [XmlAttribute]
        public string model { get; set; }

        public Engine()
        {
        }

        public Engine(double displacement, double horsePower, string model)
        {
            this.displacement = displacement;
            this.horsePower = horsePower;
            this.model = model;
        }

        public int CompareTo(object obj)
        {
            if (obj == this) return 0;
            if (obj is Engine)
            {
                return horsePower.CompareTo((obj as Engine).horsePower);
            }
            return -1;
        }

        public override string ToString()
        {
            return model + " " + displacement + " (" + horsePower + " hp)";
        }
    }
}
