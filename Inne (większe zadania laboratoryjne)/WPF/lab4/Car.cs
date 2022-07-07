using System;
using System.Collections.Generic;
using System.Text;
using System.Xml.Serialization;

namespace lab4
{
    [XmlType(TypeName = "car")]
    public class Car : IComparable
    {
        public string model { get; set; }
        [XmlElement("engine")]
        public Engine motor { get; set; }
        public int year { get; set; }

        public Car()
        {
            motor = new Engine();
        }

        public Car(string model, Engine motor, int year)
        {
            this.model = model;
            this.motor = motor;
            this.year = year;
        }

        public override string ToString()
        {
            return model + ", " + motor + ", " + year;
        }

        public int CompareTo(object obj)
        {
            return motor.CompareTo(obj);
        }
    }
}
