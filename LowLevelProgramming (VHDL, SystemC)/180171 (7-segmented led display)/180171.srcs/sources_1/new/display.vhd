----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date: 03/29/2023 08:18:26 AM
-- Design Name: 
-- Module Name: display - Behavioral
-- Project Name: 
-- Target Devices: 
-- Tool Versions: 
-- Description: 
-- 
-- Dependencies: 
-- 
-- Revision:
-- Revision 0.01 - File Created
-- Additional Comments:
-- 
----------------------------------------------------------------------------------


library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

-- Uncomment the following library declaration if using
-- arithmetic functions with Signed or Unsigned values
--use IEEE.NUMERIC_STD.ALL;

-- Uncomment the following library declaration if instantiating
-- any Xilinx leaf cells in this code.
--library UNISIM;
--use UNISIM.VComponents.all;

entity display is
    Port ( clk_i : in STD_LOGIC;
           rst_i : in STD_LOGIC;
           digit_i : in STD_LOGIC_VECTOR (31 downto 0);
           led7_an_o : out STD_LOGIC_VECTOR (3 downto 0);
           led7_seg_o : out STD_LOGIC_VECTOR (7 downto 0)
           );
end display;

architecture Behavioral of display is

signal select_led : STD_LOGIC_VECTOR (3 downto 0) := "0111";
begin
    process (clk_i, rst_i) 
    begin
        if rising_edge(clk_i) then
            if (select_led = "1111") then select_led <= "1110";
            else select_led <= select_led(0) & select_led(3 downto 1); end if;
        end if;
        if rst_i = '1' then select_led <= "1111"; end if;
    end process;
    
    led7_an_o <= select_led;
    
    with select_led select led7_seg_o <=
        digit_i(7 downto 0) when "1110",
        digit_i(15 downto 8) when "1101",
        digit_i(23 downto 16) when "1011",
        digit_i(31 downto 24) when others;
        

end Behavioral;
