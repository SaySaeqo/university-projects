library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use ieee.numeric_std.all;


entity b is
--  port ();
end entity;

architecture beh of b is

  component top is
    port (  
         clk_i : in STD_LOGIC;
         RXD_i : in STD_LOGIC;
         TXD_o : out STD_LOGIC;
         ld0 : out STD_LOGIC;
         led7_an_o : out STD_LOGIC_VECTOR (3 downto 0);
         led7_seg_o : out STD_LOGIC_VECTOR (7 downto 0)
         );
  end component;

    signal clk_i : STD_LOGIC := '0';
    signal RXD_i : STD_LOGIC := '1';
    signal TXD_o : STD_LOGIC := '1';
    signal ld0 : STD_LOGIC := '0';
    signal led7_an_o : STD_LOGIC_VECTOR (3 downto 0);
    signal led7_seg_o : STD_LOGIC_VECTOR (7 downto 0);
    

begin

    X3: top port map (clk_i => clk_i, RXD_i => RXD_i, TXD_o => TXD_o, ld0 => ld0, led7_an_o => led7_an_o, led7_seg_o => led7_seg_o);

  clk_i <= not clk_i after 5 ns;
  
  
  process
  variable counter : integer := 0;
  constant val1 : STD_LOGIC_VECTOR(9 downto 0) := "1110010100";
  constant val2 : STD_LOGIC_VECTOR(9 downto 0) := "1101010100";
  constant val3 : STD_LOGIC_VECTOR(9 downto 0) := "1001101010";
  begin
    -- RXD 1100 1010
    for i in 0 to 9 loop
        RXD_i <= val1(i);
        wait for 104_160 ns;
    end loop;
    
    wait for 2_000_000 ns;
    for i in 0 to 9 loop
        RXD_i <= val2(i);
        wait for 99_994 ns;
    end loop;
    
    wait for 2_000_000 ns;
    for i in 0 to 9 loop
        RXD_i <= val3(i);
        wait for 108_326 ns;
    end loop;
    wait for 2_000_000 ns;
    
  end process;
  

end beh;
