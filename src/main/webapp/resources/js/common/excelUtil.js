var excel = {
		download : function(paramJson){
			if( !paramJson.excelId ){
				alert('엑셀 ID가 지정되지 않았습니다.');
				return false;
			}else if( !paramJson.excelNm ){
				alert('엑셀 이름이 지정되지 않았습니다.');
				return false;
			}
			
			
			$('#excelFrame').remove();
			$('#excelForm').remove();
			$('<iframe id="excelFrame" name="excelFrame" style="display:none;">').appendTo('body');
			$('<form>').attr({
					'id' : 'excelForm',
					'action' : _CONTEXT_PATH + 'excel/download.do',
					'target' : 'excelFrame',
					'method' : 'post'
					}).appendTo('body');
			
			
			for( var key in paramJson ){
				$('<input>').attr({
					'type' : 'hidden',
					'name' : key,
					'value' : paramJson[key]
				}).appendTo('form#excelForm');
			}
			$('form#excelForm').submit();
			
		},
		uploadPop : function(excelType, confirmCallback){		// EX001
			this.uploadPopClose();
			
			
			var $div = $('<div id="pop_excelUpload" style="text-align:center;">');
			$('<input type="button" id="pop_btnSelect" value="파일 선택">').appendTo($div);
			$('<input type="text" id="pop_fileName" value="" readonly="readonly" style="width:340px;margin-left:5px;">').appendTo($div);
			$('<input type="file" id="pop_file" name="file" style="display:none;" accept="application/vnd.ms-excel">').appendTo($div);
			$('<input type="button" id="pop_btnConfirm" value="업로드" style="margin-left:5px;">').appendTo($div);
			$('<input type="button" id="pop_btnClose" value="닫기" style="margin-left:5px;">').appendTo($div);

			// 선택 버튼 -> file 클릭 이벤트
			$('#pop_btnSelect', $div).click(function(){
				$('#pop_file', $div).click();
			});
			$('#pop_file', $div).change(function(e){
				$('#pop_fileName').val($('#pop_file')[0].files[0].name);
			})
			$('#pop_btnConfirm', $div).click(function(){
				if( $('#pop_file')[0].files[0] ){
					var formData = new FormData();
					formData.append("file", $("#pop_file")[0].files[0]);
					formData.append('excelType', excelType);
					 $.ajax({
			                url: _CONTEXT_PATH+'excel/upload.json',
			                processData: false,
		                    contentType: false,
			                data: formData,
			                type: 'POST',
			                success: confirmCallback
			            });
				}else{
					alert('파일을 선택하여 주십시오.');
				}
			})
			$('#pop_btnClose', $div).click(this.uploadPopClose);
			
			
			$div.appendTo('#wrap');
			$('#wrap #pop_excelUpload').dialog({
				title:'엑셀 업로드',
				autoOpen: false,
				resizable: false,
				height: "80",
				width: 600,
				modal: true
			}).dialog('open');
		},
		uploadPopClose : function(){
			if( $("#pop_excelUpload").hasClass('ui-dialog-content') ){
				try{
					$("#pop_excelUpload").dialog('destroy');
				}catch(e){}
				$("#pop_excelUpload").remove();
			}
		}
}