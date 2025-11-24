//-----------------------------------------------------
// A 4 bit up-counter with synchronous active high reset
// and with active high enable signal
// Example from www.asic-world.com
//-----------------------------------------------------
#include "systemc.h"

SC_MODULE (gray_counter) {
  sc_in_clk     clock ;      // Clock input of the design
  sc_in<bool>   reset ;      // active high, synchronous Reset input  
  sc_out<sc_uint<3> > counter_out; // 8 bit vector output of the Johnson's counter

  //------------Local Variables Here---------------------
  sc_uint<3>	count;
  sc_uint<3>	gray;
  //sc_string 	gray ("0bus0000");

  //------------Code Starts Here-------------------------
  // Below function implements actual counter logic
  sc_uint<3> myXOR(sc_uint<3> p, sc_uint<3> q){
	  return (p^q)&(~(p&q));
  }
  void gray_process () {
    // At every rising edge of clock we check if reset is active    
    if (reset.read() == 1) {
      count =  0;
      counter_out.write(count);    
    }     
    else {
      // Calculate gray representation
      gray = myXOR(count, count>>1);

      // Count till 8
      if (count < 8) count++;
      
      // Write the gray value to output port
      counter_out.write(gray);
      cout<<"@" << sc_time_stamp() <<" :: Incremented Counter "
        <<counter_out.read()<<endl;
    }
  } // End of function johnson_process

  // Constructor for the counter
  // Since this counter is a positive edge trigged one,
  // We trigger the below block with respect to positive
  // edge of the clock and also when ever reset changes state
  SC_CTOR(gray_counter) {
    cout<<"Executing new"<<endl;
    SC_METHOD(gray_process);
    sensitive << reset;
    sensitive << clock.pos();
  } // End of Constructor

}; // End of Module 
