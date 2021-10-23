

$(document).ready(function() { 

$('#runAnalysis').prop('disabled', true);
$('#downloadTS').prop('disabled', true);
$('#loader').hide();

var oldWSDLUploaded = false;
var newWSDLUploaded = false;
var oldTSUploaded = false;
var analysisDone = false;


$('#oldWSDLUploadForm').submit(function(event) {
	
    var formElement = this;
	
    // You can directly create form data from the form element
    // (Or you could get the files from input element and append them to FormData as we did in vanilla javascript)
    var formData = new FormData(formElement);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/fileupload/wsdl/old/",
        data: formData,
        processData: false,
        contentType: false,
        success: function (response) {
            console.log(response);
			oldWSDLUploaded =true;
			
			
			if(newWSDLUploaded && oldTSUploaded && oldWSDLUploaded)
			{
				$('#runAnalysis').prop('disabled', false);
			}
			
			$('#fileUploadModal').modal('show');
            // process response
        },
        error: function (error) {
            console.log(error);
			alert("File upload failed");
            // process error
        }
    });

    event.preventDefault();
});



$('#newWSDLUploadForm').submit(function(event) {
	
    var formElement = this;
	
    // You can directly create form data from the form element
    // (Or you could get the files from input element and append them to FormData as we did in vanilla javascript)
    var formData = new FormData(formElement);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/fileupload/wsdl/new/",
        data: formData,
        processData: false,
        contentType: false,
        success: function (response) {
            console.log(response);
			newWSDLUploaded =true;
			
			
			if(newWSDLUploaded && oldTSUploaded && oldWSDLUploaded)
			{
				$('#runAnalysis').prop('disabled', false);
			}
			
			$('#fileUploadModal').modal('show');
            // process response
        },
        error: function (error) {
            console.log(error);
			alert("File upload failed");
            // process error
        }
    });

    event.preventDefault();
});



$('#oldTestCaseUploadForm').submit(function(event) {
	
    var formElement = this;
	
    // You can directly create form data from the form element
    // (Or you could get the files from input element and append them to FormData as we did in vanilla javascript)
    var formData = new FormData(formElement);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/fileupload/testcases/old/",
        data: formData,
        processData: false,
        contentType: false,
        success: function (response) {
            console.log(response);
			oldTSUploaded = true;
			
			if(newWSDLUploaded && oldTSUploaded && oldWSDLUploaded)
			{
				$('#runAnalysis').prop('disabled', false);
			}
			
			$('#fileUploadModal').modal('show');
            // process response
        },
        error: function (error) {
            console.log(error);
			alert("File upload failed");
            // process error
        }
    });

    event.preventDefault();
});


  $("#runAnalysis").click(function(){
  
  $('#order').css('filter','blur(6px)');
  
	$('#loader').show();
	
	setTimeout(function(){},5000);
	  
    $.get("/analyse_changes/", function(data, status){
		
		$('#report').empty();
		$('#downloadTS').prop('disabled', false);
		var report = "";
		report = "<div class='row' ><div class='col-md-4' ><label> <strong>Operations Added</strong></div><div class='col-md-8' > : "+data.operationsAdded +"</label></div></div>"
		report = report + "<br>"
		report = report + "<div class='row' ><div class='col-md-4' ><label> <strong>Operations Removed</strong></div><div class='col-md-8' > : "+data.operationsRemoved +"</label></div></div>"
		report = report + "<br>"
		report = report + "<div class='row' ><div class='col-md-4' ><label> <strong>Total Operations</strong></div><div class='col-md-8' > : "+data.totalOperationsInUpdatedWSDL +"</label></div></div>"
		report = report + "<br>"
		report = report + "<div class='row' ><div class='col-md-4' ><label> <strong>Operations without TestCases</strong></div><div class='col-md-8' > : "+data.testCasesMissingOperations +"</label></div></div>"
		report = report + "<br>"
		report = report + "<div class='row' ><div class='col-md-4' ><label> <strong>Test Cases coverage</strong></div><div class='col-md-8' > : "+data.testCoveragePct +"%</label></div></div>"
		report = report + "<br>"
		
		//$('#report').append(JSON.stringify(data, null, "<br>"))
		$('#report').append(report);
		
		$('#loader').hide();
		  $('#order').css('filter','blur(0px)');
			
    }); 
  });

  $("#downloadTS").click(function(){
    $.get("/downloadFile/testcases/new/", function(data, status){
		
					var fileName = "new_testcases.txt"
					var url = "Files/" + fileName;
		
		            var blob = new Blob([data], { type: "application/octetstream" });
 
                    //Check the Browser type and download the File.
                    var isIE = false || !!document.documentMode;
                    if (isIE) {
                        window.navigator.msSaveBlob(blob, fileName);
                    } else {
                        var url = window.URL || window.webkitURL;
                        link = url.createObjectURL(blob);
                        var a = $("<a />");
                        a.attr("download", fileName);
                        a.attr("href", link);
                        $("body").append(a);
                        a[0].click();
                        $("body").remove(a);
                    }
					
      //alert("Data: " + data + "\nStatus: " + status);
    });
  });
  

});
  