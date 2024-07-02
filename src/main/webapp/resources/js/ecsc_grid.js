var GV_GRID_ID = "dataGridList";        // Default Grid ID
var GV_PAGE_ID = "dataGridPageNavi";    // Default Page Navigation ID

function gf_initMyGrid(opt) {
    opt = !gf_isNull(opt) ? opt : {};
    
    var gridId = !gf_isNull(opt.gridId) ? opt.gridId : GV_GRID_ID;    // (선택) Data Grid Table ID
    var pageId = !gf_isNull(opt.pageId) ? opt.pageId : GV_PAGE_ID;    // (선택) Page Navigation Area ID
    var pageYn = !gf_isNull(opt.pageYn) ? opt.pageYn : "Y";           // (선택) Data Grid Page Navigation Y/N
    var appYn  = !gf_isNull(opt.appYn)  ? opt.appYn  : "N";           // (선택) Data Grid Table Append Y/N
    
    if(gf_isNull(gridId)){
        return null;
    }
    
    return new MyGrid(gridId, pageId, pageYn, appYn);
}

function MyGrid(gridId, pageId, pageYn, appYn) {
    this.dataGridId = gridId;    // Data Grid Table ID
    this.pageNaviId = pageId;    // Page Navigation Area ID
    this.pageYn     = pageYn;    // Data Grid Page Navigation Y/N
    this.dataAppYn  = appYn;     // Data Grid Table Append Y/N
    
    this.gridObj  = $("#" + this.dataGridId);
    this.theadObj = $("thead", this.gridObj);
    this.tbodyObj = $("tbody", this.gridObj);
    this.tfootObj = $("tfoot", this.gridObj);
    this.pageObj  = $("#" + this.pageNaviId);
    
    
    // Simple Grid 1 !!
    this.makeList = function(opt) {
        var listHtml  = !gf_isNull(opt.listHtml)  ? opt.listHtml  : "";     // (필수) List Html
        var pageFunc  = !gf_isNull(opt.pageFunc)  ? opt.pageFunc  : "";     // (필수) Page Link Function = Search Function과 동일
        var pageInfo  = !gf_isNull(opt.pageFunc)  ? opt.pageInfo  : "";     // (필수) Page Information Object
        var listChkId = !gf_isNull(opt.listChkId) ? opt.listChkId : "";     // (선택) List 전체 체크박스가 있을 경우
        var callback  = !gf_isNull(opt.callback)  ? opt.callback  : "N";    // (선택) List 뿌려준 후 Callback Function
        
        if(listHtml != ""){
            // Data Grid List Append or Paging !!
            if(this.dataAppYn == "Y"){
                this.tbodyObj.append(listHtml);
            } else {
                this.tbodyObj.html(listHtml);
                
                // 전체 체크 해제 !!
                if(listChkId != ""){
                    this.allCheckediCheck(listChkId, false);
                }
            }
            
            // Page Information HTML Setting
            if(this.pageYn == "Y"){
                if(!gf_isNull(pageInfo)){
                    this.makePageNvai(pageInfo, pageFunc);
                }
            }
        } else {
            // No Data !!
            listHtml += '<tr>';
            listHtml += '    <td id="noResultList" colspan="' + $("#" + this.dataGridId + " thead th").length + '" style="text-align:center;">검색 결과가 없습니다.</td>';
            listHtml += '</tr>';
            
            this.tbodyObj.html(listHtml);   // Data Grid Table Initialize !!
            
            if(this.pageYn == "Y"){
                this.pageObj.html("");      // Page Navigation Area Initialize !!
            }
        }
        
        // List 처리 후 Callback Function !!
        if(callback == "Y"){
            makeDataGridListAfter(this.dataGridId);
        }
    };
    
    // Simple Grid 2 !!
    this.makeSimpleList = function(opt) {
        var columns   = !gf_isNull(opt.columns)   ? opt.columns   : this.getColumns();  // (선택) Data Grid Table Columns
        var linkFunc  = !gf_isNull(opt.linkFunc)  ? opt.linkFunc  : "";                 // (선택) List Link Function
        var colFunc   = !gf_isNull(opt.colFunc)   ? opt.colFunc   : "";                 // (선택) Column Link Function
        var colFuncId = !gf_isNull(opt.colFuncId) ? opt.colFuncId : "";                 // (선택) Column Link Function ID : colFunc 있으면 (필수)
        var pageFunc  = !gf_isNull(opt.pageFunc)  ? opt.pageFunc  : "";                 // (필수) Page Link Function = Search Function과 동일
        var listChkId = !gf_isNull(opt.listChkId) ? opt.listChkId : "";                 // (선택) List 전체 체크박스가 있을 경우
        var callback  = !gf_isNull(opt.callback)  ? opt.callback  : "N";                // (선택) List 뿌려준 후 Callback Function
        
        var dataInfos = opt.dataInfos;        // (필수) dataInfos : listInfo, pageInfo 등등
        var listInfo  = dataInfos.listInfo;   // Data Grid List Information Object
        var pageInfo  = dataInfos.pageInfo;   // Page Information Object
        
        var listHtml = "";
        
        if(!gf_isNull(listInfo) && listInfo.length > 0){
            for(var i=0; i<listInfo.length; i++){
                var listMap = listInfo[i];
                
                if(linkFunc != ""){
                    listHtml += '<tr OnClick="gf_selectComLinkList(' + i + ',' + linkFunc + ');">';
                } else {
                    listHtml += '<tr>';
                }
                
                for(var j=0; j<columns.length; j++){
                    var arrColumns = columns[j].split(":");     // : 뒤에 오는 Column ID 는 모두 Hidden 값
                    
                    var column = arrColumns[0];
                    var value  = listMap[column];
                    var val    = !gf_isNull(value) ? value : "";
                    
                    
                    listHtml += '<td title="' + val  + '">';
                    
                    if(colFunc != "" && colFuncId != "" && column == colFuncId){
                        listHtml += '<a href="javascript:;" OnClick="gf_selectComLinkList(' + i + ',' + colFunc + ');">' + val + '</a>';
                    } else {
                        listHtml += val;
                    }
                    
                    // Hidden Value Setting !!
                    if(!gf_isNull(arrColumns[1])){
                        var arrHidden = arrColumns[1].split(",");
                        
                        for(var k=0; k<arrHidden.length; k++){
                            var hCol = arrHidden[k];
                            var hVal = listMap[hCol];
                            
                            listHtml += '<input id="' + hCol + '" name="' + hCol + '" type="hidden" value="' + hVal + '" />';
                        }
                    }
                    
                    listHtml += '</td>';
                }
                
                listHtml += '</tr>';
            }
            
            
            // Data Grid List Append or Paging !!
            if(this.dataAppYn == "Y"){
                this.tbodyObj.append(listHtml);
            } else {
                this.tbodyObj.html(listHtml);
                
                // 전체 체크 해제 !!
                if(listChkId != ""){
                    this.allCheckediCheck(listChkId, false);
                }
            }
            
            // Page Information HTML Setting
            if(this.pageYn == "Y"){
                if(!gf_isNull(pageInfo)){
                    this.makePageNvai(pageInfo, pageFunc);
                }
            }
        } else {
            // No Data !!
            listHtml += '<tr class="odd">';
            listHtml += '    <td valign="top" colspan="' + columns.length + '" class="dataTables_empty">No data available in table</td>';
            listHtml += '</tr>';
            
            this.tbodyObj.html(listHtml);   // Data Grid Table Initialize !!
            
            if(this.pageYn == "Y"){
                this.pageObj.html("");      // Page Navigation Area Initialize !!
            }
        }
        
        // List 처리 후 Callback Function !!
        if(callback == "Y"){
            makeDataGridListAfter(this.dataGridId);
        }
    };
    
    // Multiple Grid !!
    this.makeMultiList = function(opt) {
        var columns   = !gf_isNull(opt.columns)   ? opt.columns   : this.getColumns();  // (선택) Data Grid Table Columns
        var linkFunc  = !gf_isNull(opt.linkFunc)  ? opt.linkFunc  : "";                 // (선택) List Link Function
        var colFunc   = !gf_isNull(opt.colFunc)   ? opt.colFunc   : "";                 // (선택) Column Link Function
        var colFuncId = !gf_isNull(opt.colFuncId) ? opt.colFuncId : "";                 // (선택) Column Link Function ID
        var pageFunc  = !gf_isNull(opt.pageFunc)  ? opt.pageFunc  : "";                 // (필수) Page Link Function = Search Function과 동일
        var listChkId = !gf_isNull(opt.listChkId) ? opt.listChkId : "";                 // (선택) List 전체 체크박스가 있을 경우
        var callback  = !gf_isNull(opt.callback)  ? opt.callback  : "N";                // (선택) List 뿌려준 후 Callback Function
        
        var dataInfos = opt.dataInfos;        // (필수) dataInfos : listInfo, pageInfo 등등
        var listInfo  = dataInfos.listInfo;   // Data Grid List Information Object
        var pageInfo  = dataInfos.pageInfo;   // Page Information Object
        
        var listHtml = "";
        
        if(!gf_isNull(listInfo) && listInfo.length > 0){
            for(var i=0; i<listInfo.length; i++){
                var listMap = listInfo[i];
                
                if(linkFunc != ""){
                    listHtml += '<tr OnClick="gf_selectComLinkList(' + i + ',' + linkFunc + ');">';
                } else {
                    listHtml += '<tr>';
                }
                
                for(var j=0; j<columns.length; j++){
                    var arrIds = columns[j].split(" ");
                    
                    var colIds  = arrIds[0];    // Column ID
                    var colType = arrIds[1];    // Column Type
                    
                    var arrColumns = colIds.split(":");     // : 뒤에 오는 Column ID 는 모두 Hidden 값
                    var arrColType = !gf_isNull(colType) ? colType.split(":") : ["na"];
                    
                    var column = arrColumns[0];
                    var value  = colType == "checkbox" || colType == "radio" ? "0" : listMap[column];
                    var val    = !gf_isNull(value) ? value : "";
                    
                    // 개별 추가 Attribute 설정
                    var thObj   = $("tr:eq(0) th:eq(" + j + ")", this.theadObj);    // th Object
                    var options = !gf_isNull(thObj.attr("options")) ? thObj.attr("options") : "";
                    
                    listHtml += '<td title="' + val  + '">';
                    
                    if(arrColType[0] == "text"){
                        listHtml += '<input id="' + column + '" name="' + column + '" type="text" value="' + val + '" ' + options + ' />';
                    } else if(arrColType[0] == "checkbox" || arrColType[0] == "radio"){
                        listHtml += '<div class="form-group no-margin">';
                        listHtml += '    <div class="form-group-item">';
                        listHtml += '        <label><input id="' + column + '" name="' + column + '" type="' + colType + '" class="minimal" value="' + val + '" /></label>';
                        listHtml += '    </div>';
                        listHtml += '</div>';
                    } else if(arrColType[0] == "select"){
                        listHtml += '<select id="' + column + '" name="' + column + '" ' + options + '>';
                        
                        // ComboBox 설정 후 Html 에 포함.
                        var comboObj = $("#" + arrColType[1]);
                        
                        $("option", comboObj).each(function(){
                            if($(this).val() == val){
                                $(this).attr("selected", "selected");
                            } else {
                                $(this).removeAttr("selected");
                            }
                        });
                        
                        listHtml += comboObj.html();
                        
                        listHtml += '</select>';
                    } else {
                        if(colFunc != "" && colFuncId != "" && column == colFuncId){
                            listHtml += '<a href="javascript:;" OnClick="gf_selectComLinkList(' + i + ',' + colFunc + ');">' + val + '</a>';
                        } else {
                            listHtml += val;
                        }
                    }
                    
                    // Hidden Value Setting !!
                    if(!gf_isNull(arrColumns[1])){
                        var arrHidden = arrColumns[1].split(",");
                        
                        for(var k=0; k<arrHidden.length; k++){
                            var hCol = arrHidden[k];
                            var hVal = listMap[hCol];
                            
                            listHtml += '<input id="' + hCol + '" name="' + hCol + '" type="hidden" value="' + hVal + '" />';
                        }
                    }
                    
                    // 마지막 열인 경우 : 임의의 Hidden 값 crudFlag 추가 !!
                    if((j+1) == columns.length){
                        listHtml += '<input id="crudFlag" name="crudFlag" type="hidden" value="R" />';
                    }
                    
                    listHtml += '</td>';
                }
                
                listHtml += '</tr>';
            }
            
            
            // Data Grid List Append or Paging !!
            if(this.dataAppYn == "Y"){
                this.tbodyObj.append(listHtml);
            } else {
                this.tbodyObj.html(listHtml);
                
                // 전체 체크 해제 !!
                if(listChkId != ""){
                    this.allCheckediCheck(listChkId, false);
                }
            }
            
            // Page Information HTML Setting
            if(this.pageYn == "Y"){
                if(!gf_isNull(pageInfo)){
                    this.makePageNvai(pageInfo, pageFunc);
                }
            }
        } else {
            // No Data !!
            listHtml += '<tr class="odd">';
            listHtml += '    <td valign="top" colspan="' + columns.length + '" class="dataTables_empty">No data available in table</td>';
            listHtml += '</tr>';
            
            this.tbodyObj.html(listHtml);   // Data Grid Table Initialize !!
            
            if(this.pageYn == "Y"){
                this.pageObj.html("");      // Page Navigation Area Initialize !!
            }
        }
        
        // List 처리 후 Callback Function !!
        if(callback == "Y"){
            makeDataGridListAfter(this.dataGridId);
        }
        
        // iCheck for checkbox and radio inputs
        $("input[type='checkbox'].minimal, input[type='radio'].minimal", this.tbodyObj).iCheck({
            checkboxClass: "icheckbox_minimal-blue",
            radioClass   : "iradio_minimal-blue"
        });
    };
    
    // Make Page Navigation HTML !!
    this.makePageNvai = function(pageInfo, pageFunc) {
        var pageHtml = "";
        
        var totalCount = Number(pageInfo.totalCount);   // Total Count
        var currPageNo = Number(pageInfo.currPageNo);   // Current Page No
        var pageCount  = Number(pageInfo.pageCount);    // Page Count
        var blockCount = Number(pageInfo.blockCount);   // Block Count
        var totalPage  = Number(pageInfo.totalPage);    // Total Page
        
        var strtNo = 0;     // 중간 Block 처리 시작 No
        var endNo  = 0;     // 중간 Block 처리 끝 No
        
        // 중간 Block 처리부분 시작과 끝
        strtNo = Math.floor((currPageNo - 1) / blockCount) * blockCount + 1 ;
        endNo  = strtNo + blockCount;
        
        pageHtml += '<span class="pre">';
        // 처음 페이지
        if(currPageNo == 1){
            pageHtml += '<a href="javascript:;" ><img src="/resources/images/btn_pagenavi_first.png" alt="맨 처음으로"></a>';
        } else {
            pageHtml += '<a href="javascript:;" OnClick="gf_selectComPageList(1,' + pageFunc + ');"><img src="/resources/images/btn_pagenavi_first.png" alt="맨 처음으로"></a>';
        }
        
        // 이전 페이지
        if(currPageNo > 1){
            pageHtml += '<a href="javascript:;" onClick="gf_selectComPageList(' + (Number(currPageNo) - 1) + ',' + pageFunc + ');" ><img src="/resources/images/btn_pagenavi_before.png" alt="이전으로"></a>';
        } else {
            pageHtml += '<a href="javascript:;"><img src="/resources/images/btn_pagenavi_before.png" alt="이전으로"></a>';
        }
        pageHtml += '</span>';
        
        // 중간 Block 처리 부분
        pageHtml += '<ul>';
        for(var i=strtNo; i<endNo; i++){
            if(i == currPageNo){
                pageHtml += '<li><a href="javascript:;" class="this">' + i + '</a></li>';
                GV_CURR_PAGE_NO = currPageNo;
            } else {
                pageHtml += '<li><a href="javascript:;" onClick="gf_selectComPageList(' + i + ',' + pageFunc + ');">' + i + '</a></li>';
            }
            // Start Page 와 Total Page 가 동일할 경우 Skip
            if(i == totalPage){
                break;
            }
        }
        pageHtml += '</ul>';
        
        pageHtml += '<span class="next">';
        
        // 다음 페이지
        if(currPageNo < totalPage){
            pageHtml += '<a href="javascript:;" onClick="gf_selectComPageList(' + (Number(currPageNo) + 1) + ',' + pageFunc + ');" ><img src="/resources/images/btn_pagenavi_after.png" alt="다음으로"></a>';
        } else {
            pageHtml += '<a href="javascript:;"><img src="/resources/images/btn_pagenavi_after.png" alt="다음으로"></a>';
        }
        
        // 마지막 페이지
        if(currPageNo == totalPage){
            pageHtml += '<a href="javascript:;" ><img src="/resources/images/btn_pagenavi_last.png" alt="맨 마지막으로"></a>';
        } else {
            pageHtml += '<a href="javascript:;" onClick="gf_selectComPageList(' + totalPage + ',' + pageFunc + ');"><img src="/resources/images/btn_pagenavi_last.png" alt="맨 마지막으로"></a>';
        }
        pageHtml += '</span>';
        
        this.pageObj.html(pageHtml);  // Grid Page Navigation !!
    };
    
    // Grid Columns Array Return
    this.getColumns = function() {
        var columns   = [];
        var columnObj = $("tr th", this.theadObj);
        
        var k = 0;
        
        columnObj.each(function(idx) {
            columns[idx] = $(this).attr("id");
        });
        
        return columns;
    };
    
    // Clear
    this.clear = function() {
        var titleObj = $("tr th", this.theadObj);
        
        // No Data !!
        var listHtml  = '<tr class="odd">';
            listHtml += '    <td valign="top" colspan="' + titleObj.length + '" class="dataTables_empty">No data available in table</td>';
            listHtml += '</tr>';
        
        this.tbodyObj.html(listHtml);   // Data Grid Table Initialize !!
        this.pageObj.html("");          // Page Navigation Area Initialize !!
    };
    
    // Get Text
    this.getText = function(rowIdx, colIdx){
        return $.trim($("tr:eq(" + rowIdx + ") td:eq(" + colIdx + ")", this.tbodyObj).text());
    };
    
    // Set Text
    this.setText = function(rowIdx, colIdx, val){
        $("tr:eq(" + rowIdx + ") td:eq(" + colIdx + ")", this.tbodyObj).text(val);
    };
    
    // Get Value
    this.getValue = function(rowIdx, id){
        return $.trim($("tr input[id=" + id + "]:eq(" + rowIdx + ")", this.tbodyObj).val());
    };
    
    // Set Value
    this.setValue = function(rowIdx, id, val){
        return $("tr input[id=" + id + "]:eq(" + rowIdx + ")", this.tbodyObj).val(val);
    };
    
    // Insert Row !!
    this.insertRow = function(opt){
        opt = !gf_isNull(opt) ? opt : {};
        
        var addRowId = !gf_isNull(opt.addRowId) ? opt.addRowId : "addRowHtml";
        var locOpt   = !gf_isNull(opt.locOpt)   ? opt.locOpt   : "last";
        var rowIdx   = !gf_isNull(opt.rowIdx)   ? opt.rowIdx   : -1;
        
        var trObj = $("tr", this.tbodyObj);
        
        // 검색결과가 없을 경우의 한 줄 : 해당 한 줄을 삭제해 줌 !!
        if(trObj.length == 1 && trObj.hasClass("odd")){
            trObj.remove();
        }
        
        return this.insertHtml(addRowId, locOpt, rowIdx);
    };
    
    // Insert Row Html
    this.insertHtml = function(addRowId, locOpt, rowIdx){
        var addRowIdx = -1;
        
        var addRowHtml    = $("#" + addRowId + " tbody").html();
        var selectedTrObj = $("tr:eq(" + rowIdx + ")", this.tbodyObj);
        
        if(locOpt == "before"){               // 선택한 행 before
            selectedTrObj.before(addRowHtml);
            
            addRowIdx = rowIdx;
        } else if(locOpt == "after"){         // 선택한 행 after
            selectedTrObj.after(addRowHtml);
            
            addRowIdx = rowIdx + 1;
        } else if(locOpt == "first"){         // first
            this.tbodyObj.prepend(addRowHtml);
            
            addRowIdx = 0;
        } else {                              // last : default
            this.tbodyObj.append(addRowHtml);
            
            addRowIdx = $("tr", this.tbodyObj).length - 1;
        }
        
        var addRowTrObj = $("tr:eq(" + addRowIdx + ")", this.tbodyObj);
        
        // iCheck for checkbox and radio inputs
        $("input[type='checkbox'].minimal, input[type='radio'].minimal", addRowTrObj).iCheck({
            checkboxClass: "icheckbox_minimal-blue",
            radioClass   : "iradio_minimal-blue"
        });
        
        return addRowIdx;
    };
    
    // Delete Row
    this.deleteRow = function(rowIdx){
        if(rowIdx >= 0){
            $("tr:eq(" + rowIdx + ")", this.tbodyObj).remove();
        }
    };
    
    // Delete Rows
    this.deleteRows = function(opt){
        opt = !gf_isNull(opt) ? opt : {};
        
        var checkId = !gf_isNull(opt.checkId) ? opt.checkId : "listChk";
        
        
        if(!this.checkRows(checkId)){
            return false;
        }
        
        var k = 0;
        var arrDelRowIdx = new Array();
        
        var trObj    = $("tr", this.tbodyObj);
        var trLength = trObj.length;
        
        $(trObj.get().reverse()).each(function(idx) {
            if($("input[id=" + checkId + "]", this).is(":checked")){
                var crudFlagObj = $("#crudFlag", this);
                
                var crudFlag = crudFlagObj.val();
                
                if(crudFlag == "R"){
                    crudFlagObj.val("D");    // 실제 삭제하기 위함
                    
                    $(this).hide();
                } else if (crudFlag == "C"){
                    $(this).remove();       // 행추가 한 행은 그냥 삭제
                }
            }
        });
        
        // 모든행이 삭제 되었는지 체크 !!
        var trObjAfter = $("tr", this.tbodyObj);
        
        if(trObjAfter.length == 0){
            this.clear();                           // Grid 초기화 !!
            this.allCheckediCheck(checkId, false);  // 전체 체크 해제 !!
        }
    };
    
    // List Checkbox Check
    this.checkRows = function(checkId, checkMsg){
        checkId  = !gf_isNull(checkId) ? checkId : "listChk";
        checkMsg = !gf_isNull(checkMsg) ? checkMsg : "삭제";
        
        if(this.getCheckBoxCnt(checkId) <= 0){
            alert(checkMsg + "할 데이터가 없습니다.");
            return false;
        }
        
        if(this.getCheckedCntiCheck(checkId) <= 0){
            alert("선택된 행이 없습니다.");
            return false;
        }
        
        return true;
    };
    
    // List Get Row Count
    this.getRowCount = function(){
        var rowCount = 0;
        
        var trObj = $("tr", this.tbodyObj);
        
        trObj.each(function(){
            if(!$(this).hasClass("odd")){
                rowCount++;
            }
        });
        
        return rowCount;
    }
    
    // List Total Checkbox Count
    this.getCheckBoxCnt = function(checkId){
        checkId = !gf_isNull(checkId) ? checkId : "listChk";
        
        return $("input[id=" + checkId + "]", this.tbodyObj).length;
    };
    
    // List Checkbox Checked Count
    this.getCheckedCntiCheck = function(checkId){

        
        checkId = !gf_isNull(checkId) ? checkId : "listChk";
        
        var chkObj = $("input[id=" + checkId + "]", this.tbodyObj);
        var chkCnt = 0;
        
        // 2019-08-12 이병운 리트스 전체 선택 안되서 수정  
        for(var i=0; i<chkObj.length; i++){
            var ckObjAr  =  chkObj[i];
            if(ckObjAr.value == "1"){
                chkCnt++;
            }
        }
        
        return chkCnt;
    }
    
    // 전체 체크 or 해제 시 List CheckBox 체크 or 해제 (iCheck)
    this.allListCheckediCheck = function(allChkObj){
        var allCheckId  = $(allChkObj).attr("id");
        var listCheckId = allCheckId.replace("All", "");
        
        // 전체 체크
        var allCheckBool = $(allChkObj).parent().hasClass("checked");
        
        this.allCheckediCheck(listCheckId, !allCheckBool);
        
        // 리스트 체크
        var listCheckObj = $("input[id=" + listCheckId + "]", this.tbodyObj);

        
        listCheckObj.iCheck(!allCheckBool ? "check" : "uncheck");
        listCheckObj.val(!allCheckBool ? "1" : "0");
    };
    
    // 전체 체크 체크 or 해제
    this.allCheckediCheck = function(checkId, bool){
        var allCheckId = checkId + "All";
        var allChkObj  = $("input[id=" + allCheckId + "]", this.theadObj);
        
        if(allChkObj.length > 0){
            allChkObj.iCheck(bool ? "check" : "uncheck");
            allChkObj.val(bool ? "1" : "0");
        }
    };
    
    // List 체크 박스 체크 or 해제 시 전체 체크 여부 (iCheck)
    this.allCheckedYniCheck = function(listCheckObj){
        // 리스트 체크
        var listCheckId = $.trim($(listCheckObj).attr("id"));
        
        var listCheckBool = $(listCheckObj).parent().hasClass("checked");
        
        $(listCheckObj).val(!listCheckBool ? "1" : "0");

        var rowCount   = $("tr", this.tbodyObj).length;
        var checkedCnt = this.getCheckedCntiCheck(listCheckId);
        // 전체 체크
        this.allCheckediCheck(listCheckId, rowCount == checkedCnt ? true : false);
    };
    
    
    
    
    // List Checkbox Checked Count (기본 체크박스)
    this.getCheckedCnt = function(checkId){
        checkId = !gf_isNull(checkId) ? checkId : "listChk";
        
        return $("input[id=" + checkId + "]:checked", this.tbodyObj).length;
    }
    
    // 전체 체크 or 해제 시 List CheckBox 체크 or 해제 (기본 체크박스)
    this.allListChecked = function(obj){
        var allCheckId  = $(obj).attr("id");
        var listCheckId = allCheckId.replace("All", "");
        
        var listCheckObj = $("input[id=" + listCheckId + "]", this.tbodyObj);
        
        listCheckObj.each(function() {
            if(!$(this).is(":disabled")){
                var bool = $("#" + allCheckId, this).is(":checked");
                
                $(this).prop("checked", bool);
                $(this).val(bool ? "1" : "0");
            }
        });
    };
    
    // 전체 체크 체크 or 해제 (기본 체크박스)
    this.allChecked = function(checkId, bool){
        var allCheckId = checkId + "All";
        var allChkObj  = $("input[id=" + allCheckId + "]", this.theadObj);
        
        if(allChkObj.length > 0){
            allChkObj.prop("checked", bool);
            allChkObj.val(bool ? "1" : "0");
        }
    };
    
    // List 체크 박스 체크 or 해제 시 전체 체크 여부 (기본 체크박스)
    this.allCheckedYn = function(listChkId){
        var rowCount   = $("tr", this.tbodyObj).length;
        var checkedCnt = $("input[id=" + listChkId + "]:checked", this.tbodyObj).length;
        
        var allCheckId = listChkId + "All";
        var allChkObj  = $("input[id=" + allCheckId + "]", this.theadObj);
        
        if(allChkObj.length > 0){
            allChkObj.prop(rowCount == checkedCnt ? true : false);
            allChkObj.val(rowCount == checkedCnt ? "1" : "0");
        }
    };
    
    // 저장 Data 가 있는지  체크
    this.isStoreCheck = function(){
        var bool = false;
        var cnt  = 0;
        
        $("input[id=crudFlag]", this.tbodyObj).each(function() {
            var val = $(this).val();    // Value
            
            if(val != "R" && val != "X"){
                cnt++;
            }
        });
        
        return cnt > 0 ? true : false;
    };
    
    // Grid List Json 형태 구현
    this.makeJsonList = function(){
        var arrList = [];
        
        $("tr", this.tbodyObj).each(function(trIdx) {
            var rowData = {};
            
            var trObj = this;
            
            var crudFlag = $("#crudFlag", trObj).val();
            
            if(crudFlag != "R"){
                $("input, select", this).each(function() {
                    var id  = $(this).attr("id");       // ID
                    var val = $("#" + id, trObj).val(); // Value
                    
                    rowData[id] = val;
                });
                
                arrList.push(rowData);
            }
        });
        
        return JSON.stringify(arrList);
    };
    
    // List 저장 체크 !!
    this.storeCheck = function(){
        if(this.getRowCount() == 0){
            alert("저장할 데이터가 없습니다.");
            return false;
        }
        
        if(!this.isStoreCheck()){
            alert("변경사항이 없습니다.");
            return false;
        }
        
        var bool = true;
        
        $("._required", this.tbodyObj).each(function() {
            var id  = $(this).attr("id");
            var val = $(this).val();
            
            var tagName   = $(this).prop("tagName").toUpperCase();
            var maxlength = Number($(this).attr("maxlength"));
            
            var tdIdx = $(this).closest("td").index();
            var thTxt = $("thead tr:eq(0) th:eq(" + tdIdx + ")", this.gridObj).text();    // <th> Tag Text
            
            // 필수 체크
            if(gf_isNull(val)){
                // [field Name] (은)는 필수 입력/선택입니다.
                alert(thTxt + "(은)는 필수 " + (tagName == "SELECT" ? "선택" : "입력") + "입니다.");
                $(this).focus();
                bool = false;
                return false;
            }
            
            // 입력 Byte 수 체크
            if(gf_isNull(maxlength)){
                if(gf_getStrByte(val) > maxlength){
                    alert(thTxt + "의 최대 입력 길이 초과입니다.");
                    $(this).focus();
                    bool = false;
                    return false;
                }
            }
        });
        
        return bool;
    };
    
    // data 변경 여부 !!
    this.dataChangeYn = function(obj){
        var trObj = $(obj).closest("tr");
        var trIdx = trObj.index();
        
        var crudFlagObj  = $("#crudFlag", trObj);
        
        var crudFlag = crudFlagObj.val();
        var currVal  = $(obj).val();
        var orgVal   = $(obj).closest("td").attr("title");
        
        if(crudFlag != "C"){
            crudFlagObj.val(currVal != orgVal ? "U" : "R");
        }
    }
}


/*******************************************************
 * Page Link Function : Search Function !!
 * @param {int} pageNo
 * @param {string} pageFunc
 * @description Page Index Click 시 조회 function 호출
 *******************************************************/
function gf_selectComPageList(pageNo, pageFunc){
    if(typeof(pageFunc) == "function") {
        pageFunc(pageNo);
    }
}

/*******************************************************
 * List Link Function !!
 * @param {int} idx
 * @param {string} linkFunc
 * @description List Link Function !!
 *******************************************************/
function gf_selectComLinkList(idx, linkFunc){
    if(typeof(linkFunc) == "function") {
        linkFunc(idx);
    }
}

/*******************************************************
 * List CheckBox Click 시 전체 CheckBox 체크 여부 !!
 * @param {string} dataGridId
 * @param {string} listChkId
 * @description List CheckBox Click 시 전체 CheckBox 체크 여부 !!
 *******************************************************/
/*
function gf_allCheckedYn(dataGridId, listChkId){
    var gridObj = $("#" + dataGridId);
    
    var rowCount   = $("tbody tr", gridObj).length;
    var checkedCnt = $("tbody input[id=" + listChkId + "]:checked", gridObj).length;
    
    var allChkObj = $("thead input:checkbox[id=" + listChkId + "All]", gridObj);
    
    if(allChkObj.length > 0){
        allChkObj.prop("checked", rowCount == checkedCnt ? true : false);
    }
}
*/

/*******************************************************
 * List Data 변경 여부 Setting !!
 * @param {string} dataGridId
 * @param {int}    rowIdx
 * @param {string} currVal
 * @param {string} orgVal
 * @description List Data 변경 여부 Setting !!
 *******************************************************/
/*
function gf_dataChangeYn(dataGridId, rowIdx, currVal, orgVal){
    var gridObj  = $("#" + dataGridId);
    var tbodyObj = $("tbody input[id=crudFlag]", gridObj);
    
    var crudFlag = tbodyObj.eq(rowIdx).val();
    
    if(crudFlag != "C"){
        tbodyObj.eq(rowIdx).val(currVal != orgVal ? "U" : "R");
    }
}
*/