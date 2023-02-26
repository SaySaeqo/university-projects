extends Button


var following = false
var dragging_start_position;


# Called when the node enters the scene tree for the first time.
func _ready():
	pass # Replace with function body.


func _process(_delta):
	if following:
		OS.set_window_position(OS.window_position + get_global_mouse_position() - dragging_start_position)


func _on_TitleBar_button_down():
	following = true
	dragging_start_position = get_local_mouse_position()


func _on_TitleBar_button_up():
	following = false


func _on_secret_activated():
	if get_node("..").secret:
		text = " nieWiemJakRozmawiaćZLudźmi"
	else:
		text = " Socialize Helper"
