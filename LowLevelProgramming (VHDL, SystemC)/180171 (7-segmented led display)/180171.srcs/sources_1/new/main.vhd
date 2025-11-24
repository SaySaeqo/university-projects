library IEEE;
use IEEE.STD_LOGIC_1164.ALL;


entity top is
 Port ( clk_i : in STD_LOGIC;
 btn_i : in STD_LOGIC_VECTOR(3 downto 0);
 sw_i : in STD_LOGIC_VECTOR (7 downto 0);
 led7_an_o : out STD_LOGIC_VECTOR (3 downto 0);
 led7_seg_o : out STD_LOGIC_VECTOR (7 downto 0));
end top;

architecture Behavioral of top is

    component encoder is
        port (
            number : in STD_LOGIC_VECTOR (3 downto 0);
            led_letter : out STD_LOGIC_VECTOR(7 downto 0)
        );
      end component;
      
    component display is
        port (
           clk_i : in STD_LOGIC;
           rst_i : in STD_LOGIC;
           digit_i : in STD_LOGIC_VECTOR (31 downto 0);
           led7_an_o : out STD_LOGIC_VECTOR (3 downto 0);
           led7_seg_o : out STD_LOGIC_VECTOR (7 downto 0)
           );
    end component;
      
      signal clk: STD_LOGIC := '0';
      signal digit: STD_LOGIC_VECTOR (7 downto 0) := (others => '0');
      signal digits: STD_LOGIC_VECTOR (31 downto 0) := (others => '0');
begin

    ENC: encoder port map (number => sw_i(3 downto 0), led_letter => digit);
    DIS: display port map (rst_i => '0', clk_i => clk, digit_i => digits, led7_an_o => led7_an_o, led7_seg_o => led7_seg_o);
    
    process (clk_i) 
    variable ctr : integer range 0 to 10010 := 0;
    begin
        if rising_edge(clk_i) then
            if ctr > 10000 then ctr := 0; clk <= not clk;
            else ctr := ctr + 1; end if; 
        end if;
    end process;
    
    process (clk_i, btn_i, digit, sw_i(7 downto 4))
    begin
        if rising_edge(clk_i) then
            if btn_i(0) = '1' then
                digits(7 downto 0) <= digit;
            end if;
            if btn_i(1) = '1' then
                digits(15 downto 8) <= digit;
            end if;
            if btn_i(2) = '1' then
                digits(23 downto 16) <= digit;
            end if;
            if btn_i(3) = '1' then
                digits(31 downto 24) <= digit;
            end if;
            digits(0) <= not sw_i(4);
            digits(8) <= not sw_i(5);
            digits(16) <= not sw_i(6);
            digits(24) <= not sw_i(7);
        end if;
    end process;

end Behavioral;
