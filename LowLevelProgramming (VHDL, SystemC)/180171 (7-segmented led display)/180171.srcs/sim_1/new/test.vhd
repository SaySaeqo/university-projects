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
        btn_i : in STD_LOGIC_VECTOR(3 downto 0);
         sw_i : in STD_LOGIC_VECTOR (7 downto 0);
         led7_an_o : out STD_LOGIC_VECTOR (3 downto 0);
         led7_seg_o : out STD_LOGIC_VECTOR (7 downto 0)
    );
  end component;

    signal clk_i : STD_LOGIC := '0';
    signal btn_i : STD_LOGIC_VECTOR(3 downto 0) := "0000";
    signal sw_i : STD_LOGIC_VECTOR (7 downto 0) := (others => '0');
    signal led7_an_o : STD_LOGIC_VECTOR (3 downto 0);
    signal led7_seg_o : STD_LOGIC_VECTOR (7 downto 0);
    

begin

    X3: top port map (clk_i => clk_i, btn_i => btn_i, sw_i => sw_i, led7_an_o => led7_an_o, led7_seg_o => led7_seg_o);

  clk_i <= not clk_i after 1 ps;
  
  
  process
  variable counter : integer := 0;
  begin
    sw_i(3 downto 0) <= "0001";
    btn_i(0) <= '1';
    wait for 10 ns;
    btn_i(0) <= '0';
    wait for 10 ns;
    sw_i(3 downto 0) <= "0010";
    btn_i(1) <= '1';
    wait for 10 ns;
    btn_i(1) <= '0';
    sw_i(3 downto 0) <= "0011";
    btn_i(2) <= '1';
    wait for 10 ns;
    btn_i(2) <= '0';
    wait for 10 ns;
    sw_i(3 downto 0) <= "1100";
    btn_i(3) <= '1';
    wait for 10 ns;
    btn_i(3) <= '0';
    wait for 10 ns;
    sw_i <= "10011111";
    btn_i(0) <= '1';
    wait for 10 ns;
    btn_i(0) <= '0';
    wait for 10 ns;
  end process;
  

end beh;
