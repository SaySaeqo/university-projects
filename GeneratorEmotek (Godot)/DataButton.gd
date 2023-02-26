extends Button


var begin_window_size
var expanded_window_height
var expanded = false

# Called when the node enters the scene tree for the first time.
func _ready():
	begin_window_size = OS.get_window_size()
	expanded_window_height = begin_window_size.y + 200


# Called every frame. 'delta' is the elapsed time since the previous frame.
#func _process(delta):
#	pass


func _on_DataButton_pressed():
	if expanded:
		var main = get_node("..")
		$Tween.interpolate_method(main, "resize_window_animate", \
								OS.window_size.y, begin_window_size.y, 0.3, \
								Tween.TRANS_LINEAR, Tween.EASE_OUT)
		$Tween.start()
		icon.current_frame = 0
		focus_neighbour_bottom = "."
		expanded = false
	else:
		var main = get_node("..")
		$Tween.interpolate_method(main, "resize_window_animate", \
								begin_window_size.y, expanded_window_height, 0.3, \
								Tween.TRANS_LINEAR, Tween.EASE_OUT)
		$Tween.start()
		icon.current_frame = 1
		focus_neighbour_bottom = "../List1/ItemList"
		expanded = true
