extends Button

var resizing = false
	
func _process(_delta):
	if resizing:
		var new_size = get_global_mouse_position().y - \
							get_node("..").rect_global_position.y
		get_node("..").resize_window(new_size)
		get_node("../List1").scroll_to_end()
		get_node("../List2").scroll_to_end()
		get_node("../List3").scroll_to_end()

func _on_ResizeHandle_button_down():
	resizing = true


func _on_ResizeHandle_button_up():
	resizing = false
