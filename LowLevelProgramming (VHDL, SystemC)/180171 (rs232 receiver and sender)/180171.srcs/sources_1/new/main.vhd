library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.numeric_std.ALL;


-- This version of the top module sends back graphical representation of the received character
-- accepting at most one at a time and do not wait for new line character to send data back.
-- There is no buffering of received characters implemented.
-- Rest of the implementation is as mentioned in core_s.pdf file.

entity top is
 Port (  
         clk_i : in STD_LOGIC;
         RXD_i : in STD_LOGIC;
         TXD_o : out STD_LOGIC;
         ld0 : out STD_LOGIC;
         led7_an_o : out STD_LOGIC_VECTOR (3 downto 0);
         led7_seg_o : out STD_LOGIC_VECTOR (7 downto 0)
         );
end top;

architecture Behavioral of top is

    COMPONENT char_mem
      PORT (
        clka : IN STD_LOGIC;
        addra : IN STD_LOGIC_VECTOR(11 DOWNTO 0);
        douta : OUT STD_LOGIC_VECTOR(7 DOWNTO 0)
      );
    END COMPONENT;
    
    COMPONENT fifo_mem
      PORT (
        clk : IN STD_LOGIC;
        din : IN STD_LOGIC_VECTOR(7 DOWNTO 0);
        wr_en : IN STD_LOGIC;
        rd_en : IN STD_LOGIC;
        dout : OUT STD_LOGIC_VECTOR(7 DOWNTO 0);
        full : OUT STD_LOGIC;
        empty : OUT STD_LOGIC
      );
    END COMPONENT;
      -- RXD/TXT --
      signal numbers: STD_LOGIC_VECTOR (7 downto 0) := (others => '0');
      signal received_flag: STD_LOGIC := '0';
      signal sended_flag: STD_LOGIC := '0';
      -------------
      
      -- ROM --
      signal clka : STD_LOGIC := '0';
      signal addra : STD_LOGIC_VECTOR(11 DOWNTO 0) := (others => '0');
      signal douta : STD_LOGIC_VECTOR(7 DOWNTO 0) := (others => '0');
      ---------
begin

   ROM: char_mem port map (clka => clka, addra => addra, douta => douta);
    

-- RXD INPUT
    process (clk_i, RXD_i) 
    variable clk_ctr : integer range 0 to 10418 := 0;
    variable bit_ctr : integer range 0 to 10 := 0;
    variable is_receiving : STD_LOGIC := '0';
    variable bits: STD_LOGIC_VECTOR (7 downto 0) := (others => '0');
    begin
        if rising_edge(clk_i) then
            if received_flag = sended_flag then
                -- When first 0 is detected, start receiving
                if RXD_i = '0' then is_receiving := '1'; end if;

                if is_receiving = '1' then
                    -- For start bit, wait half bit time
                    if bit_ctr = 0 and clk_ctr >  5208 then bit_ctr := bit_ctr + 1; clk_ctr := 0;
                    -- After each bit time, sample next bit
                    elsif clk_ctr > 10416 then
                        -- If all bits received, stop receiving and update numbers
                        if bit_ctr = 9 then 
                            bit_ctr := 0;
                            is_receiving := '0';
                            numbers <= bits;
                            -- Allow sending back data
                            received_flag <= not received_flag;
                        -- Else, store received bit
                        else
                            bits(bit_ctr-1) := RXD_i;
                            bit_ctr := bit_ctr + 1;
                        end if;
                        clk_ctr := 0;
                    else clk_ctr := clk_ctr + 1;
                    end if;
                end if;
            end if;
        end if;
    end process;
    
    
-- TXD OUTPUT
    process (clk_i, numbers, received_flag, sended_flag) 
    variable clk_ctr : integer range 0 to 10418 := 0; -- 9600 baud at 100MHz clock
    variable bit_ctr : integer range 0 to 12 := 0; -- Start bit + 8 data bits + stop bit
    variable star_ascii: STD_LOGIC_VECTOR(7 downto 0) := "00101010";
    variable space_ascii: STD_LOGIC_VECTOR(7 downto 0) := "00100000";
    variable CR_ascii: STD_LOGIC_VECTOR(7 downto 0) := "00001101";
    variable LF_ascii: STD_LOGIC_VECTOR(7 downto 0) := "00001010";
    variable line_ctr : integer range 0 to 17 := 0; -- 16 lines + 1 blank line at start
    variable line_bools : STD_LOGIC_VECTOR(7 downto 0) := (others=>'0'); -- Indicates which chars to print on current line
    variable char_ctr : integer range 0 to 9 := 0; -- CR LF + 8 chars per line
    begin
        if rising_edge(clk_i) then
            if sended_flag /= received_flag then
                -- Send bit after each bit time
                if clk_ctr = 0 then
                    -- First send start bit
                    if bit_ctr = 0 then
                        TXD_o <= '0';
                    -- Last send stop bit
                    elsif bit_ctr > 8 then
                        TXD_o <= '1';
                    else
                        -- SPITTING OUT THE DATA
                        -- First 2 chars of the line are always CR LF
                        if char_ctr = 0 then
                            TXD_o <= CR_ascii(bit_ctr-1);
                        elsif char_ctr = 1 then
                            TXD_o <= LF_ascii(bit_ctr-1);
                        -- Print space for line 0 and where data from ROM indicates it
                        elsif line_ctr = 0 or line_bools(7 - (char_ctr - 2)) = '0' then
                            TXD_o <= space_ascii(bit_ctr-1);
                        -- Else print either the received char or '*' if out of range
                        elsif to_integer(unsigned(numbers)) >= 32 and to_integer(unsigned(numbers)) <= 127 then
                            TXD_o <= numbers(bit_ctr-1);
                        else
                            TXD_o <= star_ascii(bit_ctr-1);
                        end if;
                        ---------------------------
                    end if;
                    bit_ctr := bit_ctr +1;
                end if;
                clk_ctr := clk_ctr + 1;
                if clk_ctr > 10416 then
                    clk_ctr := 0;
                end if;

                -- After 3 bits wait after each full line sent, get next line from ROM
                if bit_ctr > 11 then
                    bit_ctr := 0;
                    -- After 9 chars line is finished
                    if char_ctr = 9 then
                        char_ctr := 0;
                        -- ROM CLOCK --
                        clka <= '0';
                        line_bools := douta;
                        addra <= std_logic_vector(to_unsigned(to_integer(unsigned(numbers))*16 + line_ctr, size=>12));
                        --------------------
                        line_ctr := line_ctr + 1;
                        -- After 16 lines (line 0 is blank), stop sending, reset and allow receiving new data
                        if line_ctr > 16 then
                            line_ctr := 0;
                            sended_flag <= not sended_flag;
                        end if;
                    else
                        -- Toggle ROM clock in the middle of loop
                        if char_ctr = 5 then
                            clka <= '1';
                        end if;
                        char_ctr := char_ctr + 1;
                    end if;
                end if;
            end if;
        end if;
    end process;
   
    

end Behavioral;
