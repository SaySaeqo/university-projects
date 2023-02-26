extends Control


var rng = RandomNumberGenerator.new()
var emoticons_path = "user://emoticons.dat"
var file = File.new()
var scrolling_down = true
var secret = false


func load_default():
	var tmp = ""
	for word in $List1.emoticons:
		tmp += word + " "
	$List1.load_entries(tmp)
	tmp = ""
	for word in $List2.emoticons:
		tmp += word + " "
	$List2.load_entries(tmp)
	tmp = ""
	for word in $List3.emoticons:
		tmp += word + " "
	$List3.load_entries(tmp)
		
func load_entries():
	if file.open(emoticons_path, File.READ) == OK:
		$List1.load_entries(file.get_line())
		$List2.load_entries(file.get_line())
		$List3.load_entries(file.get_line())
		if file.get_line() == "scrollup":
			scrolling_down = false
		if file.get_line() == "secret":
			secret = true
		file.close()

func _ready():
	rng.randomize()
	if not file.file_exists(emoticons_path):
		load_default()
		_on_SaveEntries_pressed()
	load_entries()
	$TitleBar._on_secret_activated()
	

func _on_RandomButton_pressed():
	var my_random = rng.randi_range(0,$List1/ItemList.get_item_count() - 1)
	$RichTextLabel.bbcode_text = "[center]" + \
	 				$List1/ItemList.get_item_text(my_random) + "[/center]"
	$RichTextLabel/Timer.stop()
	
func _on_RandomButton2_pressed():
	var my_random = rng.randi_range(0,$List2/ItemList.get_item_count() - 1)
	$RichTextLabel.bbcode_text = "[center]" + \
	 				$List2/ItemList.get_item_text(my_random) + "[/center]"
	$RichTextLabel/Timer.stop()
	
func _on_RandomButton3_pressed():
	var my_random = rng.randi_range(0,$List3/ItemList.get_item_count() - 1)
	$RichTextLabel.bbcode_text = "[center]" + \
	 				$List3/ItemList.get_item_text(my_random) + "[/center]"
	$RichTextLabel/Timer.stop()


func _on_SaveEntries_pressed():
	$List1/ItemList.sort_items_by_text()
	$List2/ItemList.sort_items_by_text()
	$List3/ItemList.sort_items_by_text()
	if file.open(emoticons_path, File.WRITE) == OK:
		var my_string
		my_string = $List1.get_entries_as_string()
		file.store_line(my_string)
		my_string = $List2.get_entries_as_string()
		file.store_line(my_string)
		my_string = $List3.get_entries_as_string()
		file.store_line(my_string)
		if scrolling_down:
			file.store_line("scrolldown")
		else:
			file.store_line("scrollup")
		if secret:
			file.store_line("secret")
		file.close()


func _on_ResetEntries_pressed():
	var item_count = $List1/ItemList.get_item_count() + \
					$List2/ItemList.get_item_count() + \
					$List3/ItemList.get_item_count()
	if item_count > 0:
		$List1.clear()
		$List2.clear()
		$List3.clear()
	else:
		load_default()
	
func resize_window(new_height):
	new_height = clamp(new_height, 340, 700)
	OS.window_size.y = new_height
	rect_size.y = new_height

func resize_window_animate(new_height):
	if new_height >= 340:
		resize_window(new_height)
	else:
		OS.window_size.y = new_height
		rect_size.y = 339


func _on_scroll_setting_changed():
	scrolling_down = !scrolling_down


func _on_secret_activated():
	secret = !secret
