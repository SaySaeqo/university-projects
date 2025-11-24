library IEEE;
use IEEE.STD_LOGIC_1164.ALL;


entity top is
 Port (  
         clk_i : in STD_LOGIC;
         rst_i : in STD_LOGIC;
         RXD_i : in STD_LOGIC;
         led7_an_o : out STD_LOGIC_VECTOR (3 downto 0);
         led7_seg_o : out STD_LOGIC_VECTOR (7 downto 0)
         );
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
      signal dis_clk : STD_LOGIC := '0';
--      signal digit: STD_LOGIC_VECTOR (7 downto 0) := (others => '0');
      signal digits: STD_LOGIC_VECTOR (31 downto 0) := (others => '1');
      signal number1: STD_LOGIC_VECTOR (3 downto 0) := (others => '0');
      signal number2: STD_LOGIC_VECTOR (3 downto 0) := (others => '0');
begin

    ENC1: encoder port map (number => number2, led_letter => digits(23 downto 16));
    ENC2: encoder port map (number => number1, led_letter => digits(15 downto 8));
    DIS: display port map (rst_i => rst_i, clk_i => dis_clk, digit_i => digits, led7_an_o => led7_an_o, led7_seg_o => led7_seg_o);
    
    
-- CLOCK FOR DISPLAY PURPOSE
    process (clk_i)
    variable dis_clk_ctr : integer range 0 to 10_002 := 0;
    begin
        if rising_edge(clk_i) then
            if dis_clk_ctr > 10_000 then dis_clk_ctr := 0; clk <= not clk;
            else dis_clk_ctr := dis_clk_ctr + 1; end if;
        end if;
    end process;
   
-- RXD HANDLING
    process (clk_i, RXD_i, rst_i) 
    variable rxd_clk_ctr : integer range 0 to 10418 := 0;
    variable bit_ctr : integer range 0 to 10 := 0;
    variable is_receiving : STD_LOGIC := '0';
    variable numbers: STD_LOGIC_VECTOR (7 downto 0) := (others => '0');
    begin
        if rising_edge(clk_i) then
            -- When first 0 is detected, start receiving
            if RXD_i = '0' then is_receiving := '1'; end if;

            if is_receiving = '1' then
                -- For start bit, wait half bit time
                if bit_ctr = 0 and rxd_clk_ctr > 5208 then bit_ctr := bit_ctr + 1; rxd_clk_ctr := 0;
                -- After each bit time, sample next bit
                elsif rxd_clk_ctr > 10416 then
                    rxd_clk_ctr := 0;
                    -- If all bits received, stop receiving and update numbers
                    if bit_ctr = 9 then 
                        bit_ctr := 0;
                        is_receiving := '0';
                        number1 <= numbers(3 downto 0);
                        number2 <= numbers(7 downto 4);
                    -- Else, store received bit
                    else
                        numbers(bit_ctr-1) := RXD_i;
                        bit_ctr := bit_ctr + 1;
                    end if;
                else rxd_clk_ctr := rxd_clk_ctr + 1;
                end if;
            end if;

            -- Synchronous reset
            if rst_i = '1' then number1 <= "0000"; number2 <= "0000"; end if;
        end if;
    end process;
    

end Behavioral;
