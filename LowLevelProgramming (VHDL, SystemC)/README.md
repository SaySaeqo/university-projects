# VHDL and SystemC code examples
In this folder there are projects from university's study showing basic use of programming FPGA with VHDL language and using SystemC for testing purposes.

VHDL projects were managed by Vivado application and used with Nexys-A7 (FPGA
xc7a100tcsg324-1).

Inside each project folder there are PDF files with detailed instruction for each exercise in polish along with source files.

## Projects' rundowns
- *7-segmented led display* - allows to input 4 hexadecimal digits to be displayed at the 4 of 7-segmented led displays, also allows to asynchronously input commas 
- *rs232 receiver* - allows to display ascii code of a character provided by RS232 protocol (from computer) using 2 of 7-segmented led displays 
- *rs232 receiver and sender* - allows to display in computer's terminal graphical representation of a provided character; character is sent to FPGA using RS232 protocol then it checks within its ROM memory for graphical representation of a given character and sends lines of data back to computer using RS232 protocol (the graphical representation is just big character writed using smaller versions on multiple lines; for more details check attached PDF file)
- *sysc_example_cygwin* - example of using SystemC for testing gray code creation out of the counter (originally run on cygwin); result was tested using GTKWave app.