extends RichTextLabel

var save_text

func _on_RichTextLabel_focus_entered():
	release_focus()
	if $Timer.is_stopped():
		var my_text = bbcode_text.substr(8, bbcode_text.rfind("[") - 8)
		OS.set_clipboard(my_text)
		save_text = bbcode_text
		bbcode_text = "[center]Copied[/center]"
		$Timer.start()

func _on_Timer_timeout():
	bbcode_text = save_text


func _on_scroll_setting_changed():
	save_text = bbcode_text
	bbcode_text = "[center]Success![/center]"
	$Timer.start(2)
