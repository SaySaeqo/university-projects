extends Control

var emoticons = []
signal scroll_setting_changed
signal secret_activated

func _on_ItemList_item_activated(index):
	$ItemList.remove_item(index)


func _on_Entry_text_changed():
	var enter_position = $Entry.text.find("\n")
	if enter_position != -1: # czy enter jest?
		$Entry.text = $Entry.text.left(enter_position)
		$Entry.cursor_set_column(enter_position)
		if $Entry.text == "scroll":
			$Entry.text = ""
			emit_signal("scroll_setting_changed")
			return
		if $Entry.text == "secret":
			$Entry.text = ""
			emit_signal("secret_activated")
			return
		_on_AddEntry_pressed()


func _on_AddEntry_pressed():
	if $Entry.text.length() > 1:
		## Usun wszystko po enterze jesli byl
		var enter_position = $Entry.text.find("\n")
		if enter_position != -1: # czy enter jest?
			$Entry.text = $Entry.text.left(enter_position)
		#####################################
		## Podziel tekst po spacjach i kazdy fragment wpisz
		if " " in $Entry.text:
			var my_items = $Entry.text.split(" ")
			for my_item in my_items:
				if my_item != "":
					$ItemList.add_item(my_item, null, true)
		## jak brak spacji to dzialaj normalnie ##
		else:
			$ItemList.add_item($Entry.text, null, true)
		$Entry.text = ""
		scroll_to_end()
	
func clear():
	$ItemList.clear()

func load_entries(my_string: String):
	$Entry.text = my_string
	_on_AddEntry_pressed()

func get_entries_as_string():
	var my_string = ""
	var i = 0
	while i < $ItemList.get_item_count():
		my_string += $ItemList.get_item_text(i) + " "
		i += 1
	return my_string
	
func scroll_to_end():
	if get_node("..").scrolling_down:
		$ItemList.get_v_scroll().value = $ItemList.get_v_scroll().max_value
	else:
		$ItemList.get_v_scroll().value = $ItemList.get_v_scroll().min_value
	
func _on_ItemList_item_selected(index):
	get_node("../RichTextLabel").bbcode_text = "[center]" + \
	 				$ItemList.get_item_text(index) + "[/center]"
	get_node("../RichTextLabel/Timer").stop()
	
func _on_ItemList_focus_exited():
	$ItemList.unselect_all()
	
func _on_ItemList_focus_entered():
	if $ItemList.is_anything_selected():
		return
	$ItemList.select($ItemList.get_item_count() - 1, true)
	if $ItemList.is_anything_selected():
		$ItemList.emit_signal("item_selected", $ItemList.get_item_count() - 1)
	scroll_to_end()
