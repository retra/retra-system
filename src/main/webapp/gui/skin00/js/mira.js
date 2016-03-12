function deleteConfirm(message) {
	return confirm(message);
}

function pagePrint() {
	window.print();
	return false;
}

function goBack() {
	history.back();
	return false;
}

function checkAll(item) {
	for (var i = 0; i < item.form.elements.length; i++) {
		if(item.form.elements[i].type == 'checkbox'){
			item.form.elements[i].checked = item.checked;
		}
	}
}

function showMiraCalendarForDateAndTime(inputId, buttonId) {
    Calendar.setup({
        inputField     :    inputId,
        ifFormat       :    '%d.%m.%Y %H:%M',
		daFormat       :    '%d.%m.%Y %H:%M',
        showsTime      :    true,
        button         :    buttonId,
        singleClick    :    true,
        align          :    'cr',
        step           :    1,
        firstDay       :    1
    });
}

function showMiraCalendarForDate(inputId, buttonId) {
    Calendar.setup({
        inputField     :    inputId,
        ifFormat       :    '%d.%m.%Y',
        daFormat       :    '%d.%m.%Y',
        showsTime      :    false,
        button         :    buttonId,
        singleClick    :    true,
        align          :    'cr',
        step           :    1,
        firstDay       :    1
    });
}

function showMiraCalendarForTime(inputId, buttonId) {
	button = document.getElementById(buttonId);
	button['onclick'] = function() {
		TimeSelector.renderHours(inputId);
	}
	ts = new TimeSelector(inputId);
	ts.generate();

}


function setFocusFirstInputOfFirstForm() {

  var bFound = false;

  // for each form
  for (f=0; f < document.forms.length; f++)
  {
    // for each element in each form
    for(i=0; i < document.forms[f].length; i++)
    {
      // if it's not a hidden element
      if (document.forms[f][i].type != "hidden")
      {
        // and it's not disabled
        if (document.forms[f][i].disabled != true)
        {
            // set the focus to it
            document.forms[f][i].focus();
            var bFound = true;
        }
      }
      // if found in this element, stop looking
      if (bFound == true)
        break;
    }
    // if found in this form, stop looking
    if (bFound == true)
      break;
  }
  
}

function copyTextIntoTextArea(what, where) {
	var textArea = document.getElementById(where);
	var oldValue = textArea.value;
	var newValue = what + ' ' + oldValue;
	textArea.value = newValue;
	textArea.focus();
	return false;
}

function worklogProjectChange() {
	var project = document.getElementById("worklogProjectId");
	var activity = document.getElementById("worklogActivityId");

	var cookieDA = getCookie("retra.projectActivityRelation"+"DA");
	if (cookieDA != undefined && cookieDA != null && cookieDA != "") {
		activity.value = cookieDA;
	}
	
	for (i = 0; i < 10; i++) {
		var cookieP = getCookie("retra.projectActivityRelation"+"P"+i);
		var cookieA = getCookie("retra.projectActivityRelation"+"A"+i);
		if (cookieP != undefined && cookieP != null && cookieP != ""
			&& cookieA != undefined && cookieA != null && cookieA != ""
			&& cookieP == project.value) {
			activity.value = cookieA;
		}
	}
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
    }
    return "";
} 

window.onload=setFocusFirstInputOfFirstForm;
