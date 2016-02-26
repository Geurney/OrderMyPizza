function div_display(div_id) {
	if (document.getElementById(div_id).style.display == "block") {
		document.getElementById(div_id).style.display = "none";
	} else {
		document.getElementById(div_id).style.display = "block";
	}
}
function fieldset_enable() {
	document.getElementById('profile_fieldset').disabled = false;
	document.getElementById('submit_form').style.display = "block";
}
function delete_req(servlet, destination) {
	if (confirm('Are you sure to delete?')) {
		$.ajax({
			url : servlet,
			method : 'DELETE',
			success : function(data) {
				if (destination != null) {
					window.location.replace(destination);
				}
			},
			error : function(data) {
				alert("Error while deleting.");
			}
		});
	}
}