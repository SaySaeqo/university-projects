#!/bin/bash
				################################# NAGŁÓWEK ###############################################
				# Author:		Tomasz Piwowski (saysaeqo@gmail.com)
				# Realese Date:		26.05.2020
				# Last Modiefied By:	Tomasz Piwowski (saysaeqo@gmail.com)
				# Last Modified On:	26.05.2020
				#
				# Version:		1.0
				# Description:		Program do prostej obróbki grafiki przeznaczonej do
				# Opis			umieszczenia w sieci.
				#
				# Licensed under GPL (see /usr/share/common-licenses/GPL for more details or
				# contact # the Free Software Foundation for a copy)
####################### ZMIENNE GLOBALNE ##################################################################################

VERSION=1.0
DIR=`dirname $0`
SOURCE="$DIR/example.jpeg"
DESTINY="$DIR/works/avatar_$$.jpeg"
CONFIG="$DIR/config.txt"
MAGICK="empty"
F_KOLOR="#000000"
F_GRUBOSC=10
S_OFF=""
T_GRAVITY="North"
T_GRAVITY_TEXT="center"
T_OFF="100%"
T_KOLOR="#000000"
T_FONT="ubuntu"
R_COL_SP=""
E_KOLOR="#000000"
E_KOLOR1="#000000"
E_KOLOR2="#FFFFFF"
EC_ALPHA="10%"
ES_ALPHA="40%"
EG_ALPHA="40%"

###################### FUNKCJE ###############################################

help(){
    
    echo -n 'Składnia:'
    echo ' ./ProstyKreatorAwatarow [SOURCE_PATH] [DESTINY_PATH] [-OPTIONS]...'
    echo -e '\n\t\tSOURCE_PATH & DESTINY_PATH są opcjonalne\n(domyślnie plik example.jpeg dla SOURCE i wynik zapisany w folderze ./works)\n'
    echo ''
    echo -e 'Opcje :'
    echo ''
    echo '-h, --help			wyświetla tę pomoc'
    echo '-v, --version			wyświetla informacje o wersji i autorze'
    echo '--reset 			resetuje plik config.txt do ustawień domyślnych'
    echo ''
    echo '-m [PATH]			zmienia ścieżkę do programu ImageMagick na [PATH]'
    echo '-f [GRUBOSC] [KOLOR]		tworzy ramke wokół obrazka o zadanej grubosci'
    echo '				  i kolorze w formacie HEX (w cudzysłowie lub'
    echo '				  bez znaku #)'
    echo '-s [SZER] [WYS] [OFFSET]	skaluje z zachowaniem proporcji, następnie wycina'
    echo '				  obszar zgodnie z parametrem OFFSET, by pasował'
    echo '				  do zadanych wymiarów (domyślnie wyśrodkowuje).'
    echo '				  OFFSET dozwolony w pixelach lub procentach'
    echo '				  (100% - skrajnie prawy/dolny fragment obrazka)'
    echo '-t [TEKST] [ROZMIAR]		pisze na obrazku TEKST o podanym rozmiarze.'
    echo '     	[OFFSET] [FONT]		  OFFSET (domyślnie 100%) to odległość od górnej'
    echo '				  krawędzi obrazka. Dozwolony w px lub %.'
    echo '				  FONT (domyslnie Ubuntu) to nazwa czcionki.'
    echo '-r				wykonuje negację kolorów z obrazka'
    echo '-e [EFEKT]			nakłada na obrazek filtr z poniższej listy:'
    echo ''
    echo '	* glass [KOLOR] [ALPHA]				szyba'
    echo '	* gradient [KOLOR1] [KOLOR2] [ALPHA]		gradient'
    echo '	* [KOLOR] [ALPHA]				kolor (bez nazwy efektu)'
    echo ''
   
}

version(){
    
    echo "ProstyKreatorAwatarow $VERSION"
    echo "Licensed under GPL (see /usr/share/common-licenses/GPL for more details or contact # the Free Software Foundation for a copy)"
    echo -e "\nAuthor: Tomasz Piwowski"
    
}

reset(){
	echo "Reset pliku config.txt..."
	rm $CONFIG
	touch $CONFIG
	echo -e "MAGICK=\"empty\"\n" > $CONFIG
}

defaults(){

    if [[ ! ( -f $CONFIG ) ]]; then
	touch $CONFIG
	echo -e "MAGICK=\"empty\"\n" > $CONFIG
    else
	source $CONFIG
    fi
    
    if [[ $MAGICK == "empty" ]]
       then
	    
	zenity --question --title "ImageMagick" \
	--text "\nCzy korzystasz z zewnętrznego pliku z programem ImageMagick (https://imagemagick.org/script/download.php)?\n\t-wybierz 'NIE/NO' \
	gdy linia poleceń przyjmuje bezpośrednio komendę np. 'display logo:'" \
	--width 600
	if [ $? -eq 0 ]
		then
		MAGICK=`zenity --file-selection`
		sed -i "s,^\(MAGICK=\).*$,\1\"$MAGICK\"," $CONFIG
	else
		sed -i "s,^\(MAGICK=\).*$,\1\"\"," $CONFIG
		MAGICK=""
	fi

	
    fi
    
}

frame(){
    
    if [[ ${F_KOLOR::1} != "#" && ${F_KOLOR::3} != "rgb" ]]
    then
	F_KOLOR="#$F_KOLOR"
    fi
        
    echo "Robię ramke o grubości	$F_GRUBOSC px"
    echo "\\o  kolorze		$F_KOLOR"
    $MAGICK convert $DESTINY -colorspace RGB -bordercolor $F_KOLOR -border ${F_GRUBOSC}x${F_GRUBOSC} $DESTINY
    
}

scale(){
    echo "Skaluje do rozmiaru $S_SZER x $S_WYS"
    
    S_COL_SP=`$MAGICK identify -format "%[colorspace]" $DESTINY`
    
    $MAGICK convert $DESTINY -colorspace RGB -resize ${S_SZER}x${S_WYS}^ \
    	-colorspace $S_COL_SP $DESTINY
    
    if [[ $S_OFF ]]
    then
     	echo "\\ Offset to $S_OFF"
	WIDTH=`$MAGICK identify -format '%w' $DESTINY`
	HEIGHT=`$MAGICK identify -format '%h' $DESTINY`
	if [[ $S_OFF =~ % ]]
	then
	    
	   S_OFF=`echo $S_OFF | sed "s,\([^%]*\)%,\1,"`
	   	   
	   if [[ $WIDTH -gt $S_SZER ]]; then   
	       let S_OFF=(WIDTH-S_SZER)*S_OFF/100
	       S_OFF="+$S_OFF"
	   else
	       let S_OFF=(HEIGHT-S_WYS)*S_OFF/100
	       S_OFF="+0+${S_OFF}"
	   fi

	else
	
	   if [[ $WIDTH -gt $S_SZER ]]; then
	   	if [[ $S_OFF -gt $((WIDTH-S_SZER)) ]]; then
			S_OFF=$((WIDTH-S_SZER))
		fi
		 S_OFF="+$S_OFF"
	   else
	    	if [[ $S_OFF -gt $((HEIGHT-S_WYS)) ]]; then
			S_OFF=$((HEIGHT-S_WYS))
		fi
		S_OFF="+0+${S_OFF}"
	   fi
	   
	fi
	$MAGICK convert $DESTINY -extent ${S_SZER}x${S_WYS}${S_OFF} $DESTINY
    else
    	$MAGICK convert $DESTINY -gravity center -extent ${S_SZER}x${S_WYS}${S_OFF} $DESTINY
    fi

    
}

title(){
    if [[ ${T_KOLOR::1} != "#" && ${T_KOLOR::3} != "rgb" ]]; then
	T_KOLOR="#$T_KOLOR"
    fi
    echo "Nadaje tekst 	$T_TRESC"
    echo "\\ o rozmiarze	$T_SIZE"
    echo "\\ z offsetem 	$T_OFF"
    echo "\\ o kolorze	$T_KOLOR"
    echo "\\ o czcionce	$T_FONT"
    echo "\\ z gravity	$T_GRAVITY"
    echo "\\ z formatowaniem tekstu typu	$T_GRAVITY_TEXT"
    if [[ $T_GRAVITY_TEXT == "left" ]]; then
    	T_GRAVITY_TEXT="West"
    fi
    if [[ $T_GRAVITY_TEXT == "right" ]]; then
    	T_GRAVITY_TEXT="East"
    fi
    
    WIDTH=`$MAGICK identify -format '%w' $DESTINY`
    
    touch /tmp/tmp1.png
    touch /tmp/tmp2.png
    temp_file1="/tmp/tmp1.png"
    temp_file2="/tmp/tmp2.png"
    
    $MAGICK convert -background none -font "$T_FONT" -size ${WIDTH}x$((T_SIZE*2)) -colorspace RGB -pointsize $T_SIZE -stroke black -strokewidth 2 -gravity $T_GRAVITY_TEXT  \
     	 -blur 0x9 label:"${T_TRESC}" $temp_file2
    $MAGICK convert -background none -font "$T_FONT" -colorspace RGB -pointsize $T_SIZE -stroke none -fill "${T_KOLOR}"  \
     	label:"${T_TRESC}" $temp_file1
    $MAGICK composite -gravity $T_GRAVITY_TEXT $temp_file1 $temp_file2 $temp_file1
	
    HEIGHT=`$MAGICK identify -format '%h' $DESTINY`
    TMP_SIZE=`$MAGICK identify -format '%h' $temp_file1`
    if [[ $T_OFF =~ % ]]
    then
	    
	T_OFF=`echo $T_OFF | sed "s,\([^%]*\)%,\1,"`
	   	   
	let T_OFF=(HEIGHT-TMP_SIZE)*T_OFF/100
	let T_OFF=T_SIZE/2+T_OFF
	T_OFF="+0+${T_OFF}"

    else
	
	if [[ $T_OFF -gt $((HEIGHT-TMP_SIZE)) ]]; then
		T_OFF=$((HEIGHT-TMP_SIZE))
	fi
	let T_OFF=T_SIZE/2+T_OFF
	T_OFF="+0+${T_OFF}"
	   
    fi
    
    $MAGICK composite -gravity $T_GRAVITY -geometry $T_OFF $temp_file1 $DESTINY $DESTINY
    rm $temp_file1
    rm $temp_file2
}

reverse(){
    echo "Reverse"
    if [[ $R_COL_SP ]]
    then
    	R_COL_SP="-set colorspace $R_COL_SP"
    fi
	
    $MAGICK convert $DESTINY $R_COL_SP -negate $DESTINY
    
}

glass(){
    if [[ ${E_KOLOR::1} != "#" && ${E_KOLOR::3} != "rgb" ]]; then
	E_KOLOR="#$E_KOLOR"
    fi
    echo "Efekt szyby"
    echo "\\kolor	$E_KOLOR"
    echo "\\alpha	$ES_ALPHA"
    if [[ $ES_ALPHA =~ %$ ]]; then
    ES_ALPHA=`echo $ES_ALPHA | sed "s,\([^%]*\)%,\1,"`
    fi
    
    touch /tmp/tmp1.png
    temp_file1="/tmp/tmp1.png"
    
    WIDTH=`$MAGICK identify -format '%w' $DESTINY`
    HEIGHT=`$MAGICK identify -format '%h' $DESTINY`
    
    $MAGICK convert -size ${WIDTH}x$HEIGHT canvas:black $temp_file1
    $MAGICK convert $temp_file1 -colorspace RGB -fill "${E_KOLOR}" -opaque black $temp_file1
    
    let TMP=HEIGHT/6
    E_ITER=0
    while [[ E_ITER -lt 5 ]]; do
    	$MAGICK convert $temp_file1 -fill '#FFFFFF20' -draw "rotate -15 rectangle -300,$((TMP/2+TMP*E_ITER*5/2)) ${WIDTH},$((TMP/2+TMP+TMP*E_ITER*5/2))" -alpha off $temp_file1
	let E_ITER=E_ITER+1
    done
    
    $MAGICK composite -dissolve $ES_ALPHA -gravity center $temp_file1 $DESTINY $DESTINY
    rm $temp_file1
}

gradient(){
    if [[ ${E_KOLOR1:1} != "#" && ${E_KOLOR1::3} != "rgb" ]]; then
	E_KOLOR1="#$E_KOLOR1"
    fi
    if [[ ${E_KOLOR2::1} != "#" && ${E_KOLOR2::3} != "rgb" ]]; then
	E_KOLOR2="#$E_KOLOR2"
    fi
    echo "Efekt gradientu"
    echo "\\kolor1	$E_KOLOR1"
    echo "\\kolor2	$E_KOLOR2"
    echo "\\alpha	$EG_ALPHA"
    if [[ $EG_ALPHA =~ %$ ]]; then
    EG_ALPHA=`echo $EG_ALPHA | sed "s,\([^%]*\)%,\1,"`
    fi
    
    touch /tmp/tmp1.png
    temp_file1="/tmp/tmp1.png"
    
    WIDTH=`$MAGICK identify -format '%w' $DESTINY`
    HEIGHT=`$MAGICK identify -format '%h' $DESTINY`
    
    $MAGICK convert -size ${WIDTH}x$HEIGHT -set colorspace RGB gradient:"$E_KOLOR1"-"$E_KOLOR2" $temp_file1
    $MAGICK composite -dissolve $EG_ALPHA -gravity center $temp_file1 $DESTINY $DESTINY
    rm $temp_file1
}

color(){
    if [[ ${E_KOLOR::1} != "#" && ${E_KOLOR::3} != "rgb" ]]; then
	E_KOLOR="#$E_KOLOR"
    fi
    echo "Efekt koloru"
    echo "\\kolor	$E_KOLOR"
    echo "\\alpha	$EC_ALPHA"
    
    $MAGICK convert $DESTINY -alpha opaque -colorspace RGB -fill "${E_KOLOR}" -colorize $EC_ALPHA $DESTINY
}
    
############################### MECHANIKA ##############################################

if [ $# -ne 0 ]
then
    OPTS=( "$@" )
    ITER=0
    while [[ ${OPTS[$ITER]} && ${OPTS[$ITER]::1} != "-" ]]
    do
	if [ $ITER -eq 0 ]
	then
	    SOURCE=${OPTS[$ITER]}
	    DESTINY="${DIR}/works/avatar_$$."`echo $SOURCE | sed "s#^.*\.##"`
	elif [ $ITER -eq 1 ]
	then
      	    DESTINY=${OPTS[$ITER]}
	else
	    echo "Nieprawidłowy argument: ${OPTS[$ITER]}"
	    exit 1
	fi
	ITER=$((ITER + 1))
    done

    TMP=$ITER
    while [[ ${OPTS[$ITER]} ]]
    do
	#--------OPCJE ROZPATRYWANE W PIERWSZEJ KOLEJNOŚCI------------
	case ${OPTS[$ITER]#-} in
	    "h" | "-help") help; exit 0 ;;
	    "v" | "-version") version; exit 0 ;;
	    "-reset") reset; exit 0 ;;
	    m)
		if [[ ${OPTS[$((ITER + 1))]} =~ ^[^-] ]]
		then
		    ITER=$((ITER + 1))
		    MAGICK=${OPTS[$ITER]}
		    sed -i "s,^\(MAGICK=\).*$,\1\"$MAGICK\"," $CONFIG
		fi
		;;
	esac
       	ITER=$((ITER + 1))
    done
    
    defaults
    cp $SOURCE $DESTINY

    ITER=$TMP
     while [[ ${OPTS[$ITER]} ]]
     do
	 #---------OPCJE ROZPATRYWANE W DRUGIEJ KOLEJNOŚCI------------
	case ${OPTS[$ITER]#-} in
	    "h" | "-help" | "v" | "-version") ;;
	    f)
		if [[ ${OPTS[$((ITER + 1))]} =~ ^[0-9]+$ ]]
		then
		    ITER=$((ITER + 1))
		    F_GRUBOSC=${OPTS[$ITER]}
		fi
		
		if [[ ${OPTS[$((ITER + 1))]} =~ ^#?[0-9a-fA-F]{6} ]]
		then
		    ITER=$((ITER + 1))
		    F_KOLOR=${OPTS[$ITER]}
		fi

		if [[ -z ${OPTS[$((ITER + 1))]} || ${OPTS[$((ITER + 1))]} =~ ^-.* ]]
		then
		    frame
		fi
  		;;
	    s)
		if [[ ${OPTS[$((ITER + 1))]} =~ ^[0-9]+$ ]]
		then
		    ITER=$((ITER + 1))
		    S_SZER=${OPTS[$ITER]}
		else
		    echo "Opcja -s: brakuje parametru"
		    exit 1
		fi
		
		if [[ ${OPTS[$((ITER + 1))]} =~ ^[0-9]+$ ]]
		then
		    ITER=$((ITER + 1))
		    S_WYS=${OPTS[$ITER]}
		else
		    echo "Opcja -s: brakuje parametru"
		    exit 1
		fi
				
		if [[ ${OPTS[$((ITER + 1))]} =~ ^(([0-9]+)|(100%)|([1-9]?[0-9]%))$ ]]
		then
		    ITER=$((ITER + 1))
		    S_OFF=${OPTS[$ITER]}
		fi

		if [[ -z ${OPTS[$((ITER + 1))]} || ${OPTS[$((ITER + 1))]} =~ ^-.* ]]
		then
		    scale
		fi
		;;
	    t)
	    	if [[ ${OPTS[$((ITER + 1))]} =~ . ]]
		then
		    ITER=$((ITER + 1))
		    T_TRESC=${OPTS[$ITER]}
		else
		    echo "Opcja -t: brakuje parametru"
		    exit 1
		fi
		
		if [[ ${OPTS[$((ITER + 1))]} =~ ^[1-9][0-9]*$ ]]
		then
		    ITER=$((ITER + 1))
		    T_SIZE=${OPTS[$ITER]}
		else
		    echo "Opcja -t: brakuje parametru"
		    exit 1
		fi
				
		if [[ ${OPTS[$((ITER + 1))]} =~ ^(([0-9]+)|(100%)|([1-9]?[0-9]%))$ ]]
		then
		    ITER=$((ITER + 1))
		    T_OFF=${OPTS[$ITER]}
		fi
		
		if [[ ${OPTS[$((ITER + 1))]} =~ ^#?[0-9a-fA-F]{6} ]]
		then
		    ITER=$((ITER + 1))
		    T_KOLOR=${OPTS[$ITER]}
		fi
		
		if [[ ${OPTS[$((ITER + 1))]} =~ ^[A-Z\ a-z]+$ ]]
		then
		    ITER=$((ITER + 1))
		    T_FONT=${OPTS[$ITER]} 
		fi

		if [[ -z ${OPTS[$((ITER + 1))]} || ${OPTS[$((ITER + 1))]} =~ ^-.* ]]
		then
		    title
		fi
		;;
	   r)
	   	reverse
		;;
	   e)
	   	if [[ ${OPTS[$((ITER + 1))]} == "glass" ]]
		then
		    ITER=$((ITER + 1))
		    
		    if [[ ${OPTS[$((ITER + 1))]} =~ ^#?[0-9a-fA-F]{6} ]]
		    then
		   	 ITER=$((ITER + 1))
		   	 E_KOLOR=${OPTS[$ITER]}
		    fi
		    if [[ ${OPTS[$((ITER + 1))]} =~ ^((100%)|([1-9]?[0-9]%))$ ]]
		    then
		    	ITER=$((ITER + 1))
		    	ES_ALPHA=${OPTS[$ITER]}
		    fi
		    
		    if [[ -z ${OPTS[$((ITER + 1))]} || ${OPTS[$((ITER + 1))]} =~ ^-.* ]]
		    then
		    	glass
		    fi
		    
		elif [[ ${OPTS[$((ITER + 1))]} == "gradient" ]]
		then
		    ITER=$((ITER + 1))
		    
		    if [[ ${OPTS[$((ITER + 1))]} =~ ^#?[0-9a-fA-F]{6} ]]
		    then
		   	 ITER=$((ITER + 1))
		   	 E_KOLOR1=${OPTS[$ITER]}
		    fi
		    if [[ ${OPTS[$((ITER + 1))]} =~ ^#?[0-9a-fA-F]{6} ]]
		    then
		   	 ITER=$((ITER + 1))
		   	 E_KOLOR2=${OPTS[$ITER]}
		    fi
		    if [[ ${OPTS[$((ITER + 1))]} =~ ^((100%)|([1-9]?[0-9]%))$ ]]
		    then
		    	ITER=$((ITER + 1))
		    	EG_ALPHA=${OPTS[$ITER]}
		    fi
		    
		    if [[ -z ${OPTS[$((ITER + 1))]} || ${OPTS[$((ITER + 1))]} =~ ^-.* ]]
		    then
		    	gradient
		    fi
		    
		else
		    if [[ ${OPTS[$((ITER + 1))]} =~ ^#?[0-9a-fA-F]{6} ]]
		    then
		   	 ITER=$((ITER + 1))
		   	 E_KOLOR=${OPTS[$ITER]}
		    fi
		    if [[ ${OPTS[$((ITER + 1))]} =~ ^((100%)|([1-9]?[0-9]%))$ ]]
		    then
		    	ITER=$((ITER + 1))
		    	EK_ALPHA=${OPTS[$ITER]}
		    fi
		    
		    if [[ -z ${OPTS[$((ITER + 1))]} || ${OPTS[$((ITER + 1))]} =~ ^-.* ]]
		    then
		    	color
		    fi
		    
		fi
		;;
		
	    *) echo "Nieprawidlowy argument: ${OPTS[$ITER]#-}"; exit 1 ;;
	esac
       	ITER=$((ITER + 1))
    done

    
    
else
    #-----------------WERSJA W ZENITY-------------------------
    defaults
    cp $SOURCE $DESTINY
    KONIEC=1
    while [[ KONIEC -eq 1 ]]; do
    
    	$MAGICK display -immutable -resize 800x600\> $DESTINY &
	sleep 1
	xdotool search --name "ImageMagick: .*" windowmove 100 100
	
    	if [[ $MAGICK =~ \/.*\/.*\/ ]]; then
		Z_MAGICK=`echo $MAGICK | sed "s#^.*/\([^/]*/\)#/.../\1#"`
	else
		Z_MAGICK=$MAGICK
	fi
	if [[ $SOURCE =~ \/.*\/.*\/ ]]; then
		Z_SOURCE=`echo $SOURCE | sed "s#^.*/\([^/]*/\)#/.../\1#"`
	else
		Z_SOURCE=$SOURCE
	fi
    	if OPTION=`zenity --list --title="ProstyKreatorAwatarow" --text="Prosze wybrać opcję z poniższej listy." --column="Opcja" --column="Parametry" --hide-header --width 400 --height=400 \
    		"		version" ""\
    		"		reset" "" \
    		"MAGICK:" "$Z_MAGICK"\
   		"SOURCE:" "$Z_SOURCE"\
    		"Dodaj ramkę" ""\
    		"Przeskaluj obraz" ""\
   		"Dodaj tytuł" ""\
  		"Odwróć kolory" ""\
  		"Inne efekty" ""\
  		"ZAPISZ" ""\
  		"Cofnij zmiany" ""\
    		2>/dev/null`
    	then
	
    		if [[ $OPTION ]]
		then
    			case $OPTION in
    				"		version") 
					Z_TEKST=`version`
					zenity --info --title="version" --text="$Z_TEKST" --width 400 2>/dev/null 
					;;
    				"		reset") 
					reset
					 if zenity --question --title="Exit" --text="Reset pliku config.txt zakończony sukcesem!\nWyjść z programu?" \
						--width 400 2>/dev/null ; then
						KONIEC=0
						rm $DESTINY
					else
						defaults
					fi
					;;
    				"MAGICK:") 
					MAGICK=`zenity --file-selection --title="Ścieżka do ImageMagick" 2>/dev/null`
					sed -i "s,^\(MAGICK=\).*$,\1\"$MAGICK\"," $CONFIG
					;;
   				"SOURCE:")
					SOURCE=`zenity --file-selection --title="Ścieżka do obrazka źródłowego" 2>/dev/null`
					rm $DESTINY
					DESTINY="${DIR}/works/avatar_$$."`echo $SOURCE | sed "s#^.*\.##"`
					cp $SOURCE $DESTINY
					;;
    				"Dodaj ramkę") 
					TMP=`zenity --scale --value="$F_GRUBOSC" --max-value=25 --min-value=1 --title="Grubość ramki" --width 300 --text="Dodaj grubość ramki w pikselach." 2>/dev/null`
					if [[ $TMP ]]
					then
						F_GRUBOSC=$TMP
						TMP=`zenity --color-selection --show-palette --title="Kolor ramki" --color="$F_KOLOR" --width 300 2>/dev/null`
						if [[ $TMP ]]
						then
							F_KOLOR=$TMP
							frame
						fi
					fi
					;;
    				"Przeskaluj obraz")
					TMP=`zenity --forms --title="Skalowanie obrazu" --width 300 --text="Wymiary nowego obrazka" --add-entry="Szerokość" --add-entry="Wysokość" 2>/dev/null`
					if [[ $TMP =~ ^[0-9]+|[0-9]+$ ]]
					then
						S_SZER=`echo $TMP | sed "s#^\([0-9]*\)|\([0-9]*\)#\1#"`
						S_WYS=`echo $TMP | sed "s#^\([0-9]*\)|\([0-9]*\)#\2#"`
						TMP=`zenity --scale --value="50" --title="Ustawienie przesunięcia" --width 300 --text="0 - góra/lewo\t100 - dół/prawo" 2>/dev/null`
						if [[ $TMP ]]
						then
							S_OFF="${TMP}%"
							scale
						fi
					fi
					;;
   				"Dodaj tytuł") 
					TMP=`zenity --entry --entry-text="PKA EDITOR" --title "Treść podpisu" --text="Dodaj tytuł." --width 300 2>/dev/null`
					if [[ $TMP ]]
					then
						T_TRESC=$TMP
						TMP=`zenity --scale --value="48" --min-value=6 --max-value=142 --title="Rozmiar czcionki" --text="Dodaj rozmiar czcionki." --width 300 2>/dev/null`
						if [[ $TMP ]]
						then
							T_SIZE=$TMP
							TMP=`zenity --scale --value="100" --title="Ustawienie przesunięcia" --width 300 --text="0 - góra\t\t\t100 - dół" 2>/dev/null`
							if [[ $TMP ]]
							then
								T_OFF="${TMP}%"
								TMP=`$MAGICK identify -list font | sed "s#^   .*##" | sed "s#^  Font: \([^-]*\).*#\1#" | sed "s#^Math.*##" | sed "s#^Path:.*##" | sort -u | sed "s,^\(.\+\),FALSE \1," | sed "s,FALSE Ubuntu,TRUE Ubuntu,"`
								TMP=`zenity --list --title="Czcionka" --column="Wybór" --column="Czcionka" --print-column=2 --radiolist --width 300 --height 300 --text="Wybierz czcionkę z poniższej listy." $TMP 2>/dev/null`
								if [[ $TMP ]]
								then
									T_FONT=$TMP
									TMP=`zenity --color-selection --title="Kolor tekstu" --color="$T_KOLOR" --show-palette --width 300 2>/dev/null`
									if [[ $TMP ]]
									then
										T_KOLOR=$TMP
										TMP=`zenity --list --title="Formatowanie tekstu" --column="Wybór" --column="Formatowanie" --text="Wybierz opcję formatowania teksu z poniższej listy." --print-column=2 --radiolist --width 300 --height 200 \
										FALSE left TRUE center FALSE right \
										2>/dev/null`
										if [[ $TMP ]]
										then
											T_GRAVITY_TEXT=$TMP
											title
										fi
									fi
								fi
							fi
						fi
					fi
					;;
  				"Odwróć kolory")
					TMP=`zenity --list --title="Przestrzeń barw" --column="Wybór" --text="Wybierz przestrzeń barw z poniższej listy." --column="Przestrzeń barw" --print-column=2 --radiolist --width 300 --height 200 \
					TRUE RGB FALSE sRGB FALSE LUV FALSE LAB FALSE HSL FALSE XYZ \
					2>/dev/null`
					if [[ $TMP ]]
					then
						R_COL_SP=$TMP
						reverse
					fi
					;;
  				"Inne efekty") 
					EFEKT=`zenity --list --title="Inne efekty" --width 300 --height 200 --column="Nazwa" --text="Wybierz efekt z poniższej listy" \
					"KOLOR" "GRADIENT" "SZYBA" \
					2>/dev/null`
					case $EFEKT in
						"KOLOR")
							TMP=`zenity --color-selection --show-palette --title="Wybierz kolor" --color="$E_KOLOR" --width 300 2>/dev/null`
							if [[ $TMP ]]
							then
								E_KOLOR=$TMP
								TMP=`echo $EC_ALPHA | sed "s#%##"`
								TMP=`zenity --scale --value="$TMP" --title="Ustawianie przezroczystości" --text="Podaj przezroczystość efektu." --width 300 2>/dev/null`
								if [[ $TMP ]]
								then
									EC_ALPHA="${TMP}%"
									color
								fi
							fi
							;;
						"GRADIENT")
							TMP=`zenity --color-selection --show-palette --title="Wybierz kolor (góra)" --color="$E_KOLOR1" --width 300 2>/dev/null`
							if [[ $TMP ]]
							then
								E_KOLOR1=$TMP
								TMP=`zenity --color-selection --title="Wybierz kolor (dół)" --color="$E_KOLOR2" --width 300 2>/dev/null`
								if [[ $TMP ]]
								then
									E_KOLOR2=$TMP
									TMP=`echo $EG_ALPHA | sed "s#%##"`
									TMP=`zenity --scale --value="$TMP" --title="Ustawianie przezroczystości" --text="Podaj przezroczystość efektu." --width 300 2>/dev/null`
									if [[ $TMP ]]
									then
										EG_ALPHA="${TMP}%"
										gradient
									fi
								fi
							fi
							;;
						"SZYBA")
							TMP=`zenity --color-selection --show-palette --title="Wybierz kolor" --color="$E_KOLOR" --width 300 2>/dev/null`
							if [[ $TMP ]]
							then
								E_KOLOR=$TMP
								TMP=`echo $ES_ALPHA | sed "s#%##"`
								TMP=`zenity --scale --value="$TMP" --title="Ustawianie przezroczystości" --text="Podaj przezroczystość efektu." --width 300 2>/dev/null`
								if [[ $TMP ]]
								then
									ES_ALPHA="${TMP}%"
									glass
								fi
							fi
							;;
						*) ;;
					esac
					;;
  				"Cofnij zmiany") 
					cp $SOURCE $DESTINY
					;;
  				"ZAPISZ") 
					KONIEC=0
					if zenity --question --title="Zapisywanie pliku" --text="Czy zapisać wg ustawień niestandardowych?" --width 400 2>/dev/null
					then
						NAME=`zenity --entry --title="Nazwa" --text="Podaj nazwę do zapisu pliku:" 2>/dev/null`
						DIRECTORY=`zenity --file-selection --directory --title="Wybieranie folderu" 2>/dev/null`
						echo $NAME | grep '\.[^.]*'
						if [[ $? -eq 1 ]]
						then
							NAME="${NAME}."`echo $SOURCE | sed "s#^.*\.##"`
							echo $DIRECTORY
						fi
						cp $DESTINY ${DIRECTORY}/$NAME
						rm $DESTINY
					fi
					;;
				*) echo "ojojojo!" ;;
			esac
		else
			KONIEC=0
		fi
	else
		KONIEC=0
		rm $DESTINY
    	fi
	
	xdotool search --name "ImageMagick: .*" windowclose %@ 2>/dev/null
	xdotool search --name "ImageMagick: .*" windowclose %@ 2>/dev/null
	xdotool search --name "ImageMagick: .*" windowclose %@ 2>/dev/null
	
    done
fi

xdotool search --name "ImageMagick: .*" windowkill %@ 2>/dev/null

#$MAGICK montage -geometry 600x600\>+10 -shadow -background none -label "%f" -frame 5 $SOURCE $DESTINY show:
#rm -r $DIR/works/*

echo -e "---------------\nKoniec Programu\n---------------"
