function setPageReadOnly(target){
    // 버튼 삭제
    $("input[type=button], input[type=file], input[type=submit]", target).each(function() {
        if ($(this).data("check-mod") == "Y") {
            $(this).hide();
        }
    });

    $("span[data-check-mod=Y], a[data-check-mod=Y]", target).remove();

    // READONLY
    $("input[type=text][data-check-mod=Y], input[type=checkbox][data-check-mod=Y], input[type=radio][data-check-mod=Y], input[type=number][data-check-mod=Y], input[type=email][data-check-mod=Y], select[data-check-mod=Y], textarea[data-check-mod=Y]", target).each(function() {
        if ($(this).is(".datep")) {
            $(this).datepicker("disable");
        }

        $(this).prop("disabled", true).addClass("readonly");
    });

    $(".ui-jqgrid-btable", target).each(function() {
        if ($(this).data("check-mod") == "Y") {
            $(this)[0].p.cellEdit = false;
        }
    });
}

/**
 * Null 체크
 *
 * @param data
 * @returns {Boolean}
 */
function isNull(data) {
    if (data == null || data == undefined || data.length == 0) {
        return true;
    } else {
        return false;
    }
}

/**
 * Null값 치환
 * @param data
 * @returns
 */
function nvl(data) {
    if (isNull(data)) {
        return "";
    }

    return data;
}

/**
 * 왼쪽 채우기
 * @param data
 * @param length
 * @param rep
 * @returns {String}
 */
function lpad(data, length, rep) {
    data = String(data);
    while (data.length < length) {
        data = rep + "" + data;
    }

    return data;
}

function transDateFromApi(data) {
    if (data != "" && data != undefined) {
        var year = data.substring(0, 2) * 1;
        if (year > 50) {
            return "19" + data;
        } else {
            return "20" + data;
        }
    }
    return "";
}

function transDateFromPicker(data) {
    if (isNull(data) || data.length != 10) return data;

    return data.substring(6) + "-" + data.substring(0, 2) + "-" + data.substring(3, 5);
}

/**
 * 입력값 Byte 계산
 * @param data
 * @returns {Number}
 */
function getLengthb(data) {
    var lengthb = 0;
    for (var i = 0; data != null && i < data.length; i++) {
        var oneChar = escape(data.charAt(i));
        if (oneChar.length == 1) {
            lengthb += 1;
        } else if (oneChar.indexOf("%u") != -1) {
            lengthb += 2;
        } else if (oneChar.indexOf("%") != -1) {
            lengthb += oneChar.length / 3;
        }
    }

    return lengthb;
}

/**
 * 요소 유효성체크
 * @param eleId
 * @returns
 */
function isValidElement(eleId) {
    var bool = true;
    var msg = "";
    var eleLabel = $("label[for='" + eleId + "']").html();
    var eleMaxBytes = 0;
    var eleType = $("#" + eleId).prop("type");

    if ($("#" + eleId).attr("required")) {
        if (isNull($("#" + eleId).val())) {
            if (eleType.indexOf("text") >= 0) {
                msg += eleLabel + " (은)는 필수 입력값입니다.\n";
            } else if (eleType.indexOf("select") >= 0) {
                msg += eleLabel + " (은)는 필수 선택값입니다.\n";
            }
        }
    }

    if ($("#" + eleId).attr("maxBytes")) {
        eleMaxBytes = $("#" + eleId).attr("maxBytes");
        eleBytes = getLengthb($("#" + eleId).val());

        if (eleMaxBytes < eleBytes) {
            msg += eleLabel + " (을)를 " + $("#" + id).attr("maxBytes") + "Bytes 이내로 입력하시기 바랍니다. (현재: " + eleBytes + "Bytes)\n";
        }
    }

    if (!isNull(msg)) {
        simpleAlert(msg);
        $("#" + eleId).focus();
        bool = false;
    }

    return bool;
}

/**
 * form 요소 유효성체크
 * @param formId
 * @returns {Boolean}
 */
function isValid(formId) {
    var bool = true;
    var msg = "";
    var firstEleId = "";
    var RegExpForNumberOnly = /^[0-9]*$/;

    $("#" + formId + " input:text, #" + formId + " select, #" + formId + " textarea").each(function() {
        var eleId = $(this).attr("id");
        var eleLabel = $("label[for='" + eleId + "']").html();
        var eleMaxBytes = 0;
        var eleType = $(this).prop("type");

        if ($(this).attr("required")) {
            if (isNull($(this).val())) {
                if (eleType.indexOf("text") >= 0) {
                    msg += eleLabel + " (은)는 필수 입력값입니다.\n";
                } else if (eleType.indexOf("select") >= 0) {
                    msg += eleLabel + " (은)는 필수 선택값입니다.\n";
                }

                if (isNull(firstEleId)) {
                    firstEleId = eleId;
                }
            }
        }
        if ($(this).attr("maxBytes")) {
            eleMaxBytes = $(this).attr("maxBytes");
            eleBytes = getLengthb($(this).val());

            if (eleMaxBytes < eleBytes) {
                msg += eleLabel + " (을)를 " + $(this).attr("maxBytes") + "Bytes 이내로 입력하시기 바랍니다. (현재: " + eleBytes + "Bytes)\n";

                if (isNull(firstEleId)) {
                    firstEleId = eleId;
                }
            }
        }
        if ($(this).attr("numberOnly") != undefined) {
            if (!isNull($(this).val())) {
                if (!RegExpForNumberOnly.test($(this).val())) {
                    msg += eleLabel + " (을)를 숫자만 입력하시기 바랍니다.\n";
                }

                if (isNull(firstEleId)) {
                    firstEleId = eleId;
                }
            }
        }
    });

    var preEleId = "";

    $("#" + formId + " input:radio").each(function() {
        var eleId = $(this).attr("id");
        var eleLabel = $("label[for='" + eleId + "']").html();
        var eleMaxBytes = 0;
        var eleType = $(this).prop("type");

        if (preEleId != eleId) {
            if ($(this).attr("required")) {
                if (!$("input:radio[name=" + eleId + "]").is(":checked")) {
                    msg += eleLabel + " (은)는 필수 선택값입니다.\n";

                    if (isNull(firstEleId)) {
                        firstEleId = eleId;
                    }
                }
            }
            preEleId = eleId;
        }
    });

    if (msg != "") {
        simpleAlert(msg);
        if (!isNull(firstEleId)) {
            $("#" + firstEleId).focus();
        }
        bool = false;
    }

    return bool;
}

function isValidListForm(formId) {
    var bool = true;
    var msg = "";
    var RegExpForNumberOnly = /^[0-9]*$/;

    $("#" + formId + " input:text, #" + formId + " select, #" + formId + " textarea").each(function() {
        var rowSeq = $(this).parent().parent().find("span").html();
        var eleId = $(this).attr("id");
        var eleLabel = $(this).attr("desc");
        var eleMaxBytes = 0;
        var eleType = $(this).prop("type");

        if ($(this).attr("required")) {
            if (isNull($(this).val())) {
                if (eleType.indexOf("text") >= 0) {
                    msg += eleLabel + " (은)는 필수 입력값입니다.\n";
                } else if (eleType.indexOf("select") >= 0) {
                    msg += eleLabel + " (은)는 필수 선택값입니다.\n";
                }
            }
        }
        if ($(this).attr("maxBytes")) {
            eleMaxBytes = $(this).attr("maxBytes");
            eleBytes = getLengthb($(this).val());

            if (eleMaxBytes < eleBytes) {
                msg += rowSeq + "번째 줄 " + eleLabel + " (을)를 " + $(this).attr("maxBytes") + "Bytes 이내로 입력하시기 바랍니다. (현재: " + eleBytes + "Bytes)\n";
            }
        }
        if ($(this).attr("numberOnly") != undefined) {
            if (!isNull($(this).val())) {
                if (!RegExpForNumberOnly.test($(this).val())) {
                    msg += rowSeq + "번째 줄 " + eleLabel + " (을)를 숫자만 입력하시기 바랍니다.\n";
                }
            }
        }
    });

    var preEleId = "";

    $("#" + formId + " input:radio").each(function() {
        var eleId = $(this).attr("id");
        var eleLabel = $("label[for='" + eleId + "']").html();
        var eleMaxBytes = 0;
        var eleType = $(this).prop("type");

        if (preEleId != eleId) {
            if ($(this).attr("required")) {
                if (!$("input:radio[name=" + eleId + "]").is(":checked")) {
                    msg += eleLabel + " (은)는 필수 선택값입니다.\n";

                    if (isNull(firstEleId)) {
                        firstEleId = eleId;
                    }
                }
            }
            preEleId = eleId;
        }
    });

    if (msg != "") {
        simpleAlert(msg);
        bool = false;
    }

    return bool;
}

function getType(p) {
    if(Array.isArray(p)) {
        return "array";
    } else if(typeof p == "string") {
        return "string";
    } else if(p != null && typeof p == "object") {
        return "object";
    } else {
        return "other";
    }
}

/**
 * API 성공여부 체크
 * @param data
 * @returns {Boolean}
 */
function isApiSuccess(data) {
    if (!isNull(data) && data.responseCode == "200") {
        return true;
    }
    return false;
}

/**
 * Browser 확인하여 창 닫기
 */
function windowClose() {
    var agent = navigator.userAgent.toLowerCase();
    //$.post('/logout.json');
    //if((navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1)) { //Explorer
    //  window.close();
    //} else {
    window.open("", "_self").close();
    //}
}

function b64toBlob(b64Data, contentType, sliceSize) {
    contentType = contentType || "";
    sliceSize = sliceSize || 512;

    var byteCharacters = atob(b64Data);
    var byteArrays = [];

    for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
        var slice = byteCharacters.slice(offset, offset + sliceSize);

        var byteNumbers = new Array(slice.length);
        for (var i = 0; i < slice.length; i++) {
            byteNumbers[i] = slice.charCodeAt(i);
        }

        var byteArray = new Uint8Array(byteNumbers);

        byteArrays.push(byteArray);
    }

    var blob = new Blob(byteArrays, { type: contentType });
    return blob;
}

function pdfDownload(base64, contentType, fileName) {
    var blob = b64toBlob(base64, "application/pdf");

    if (window.navigator.msSaveOrOpenBlob) {
        window.navigator.msSaveOrOpenBlob(blob);
    } else {
        //create anchor
        var a = document.createElement("a");
        //set attributes
        a.setAttribute("href", "data:application/pdf;base64," + base64);
        a.setAttribute("download", fileName);
        //create click event
        //a.onclick = blob;

        //append, trigger click event to simulate download, remove
        document.body.appendChild(a);
        a.click();
        document.body.removeChild(a);
    }
}

function openPopupPDF(pdf) {
    var w = window.open("/pdfViewer/viewer.html?file=");
    w.addEventListener("load", function() {
            var res = pdf;

            var pdfData = base64ToUint8Array(res);
            w.PDFViewerApplication.open(pdfData);

            function base64ToUint8Array(base64) {
                var raw = atob(base64);
                var uint8Array = new Uint8Array(raw.length);
                for (var i = 0; i < raw.length; i++) {
                    uint8Array[i] = raw.charCodeAt(i);
                }
                return uint8Array;
            }
    }, true);
}

var JSONUtil = {
    rtn: [],
    rtnHistory: [],
    findKeyInValue: function(json, key, value) {
        if (json != null) {
            for (var subKey in json) {
                if ($.type(json[subKey]) == "string" && json[subKey] == value && subKey == key) {
                    JSONUtil.rtn.push(json);
                } else if ($.type(json[subKey]) == "array") {
                    $.each(json[subKey], function(n, obj) {
                        JSONUtil.findKeyInValue(obj, key, value);
                    });
                } else if ($.type(json[subKey]) == "object") {
                    JSONUtil.findKeyInValue(json[subKey], key, value);
                }
            }
        }
    },
    findKeyInValueHistory: function(json, key, value) {
        if (json != null) {
            for (var subKey in json) {
                if ($.type(json[subKey]) == "string" && json[subKey] == value && subKey == key) {
                    JSONUtil.rtnHistory.push(json);
                    return json;
                } else if ($.type(json[subKey]) == "array") {
                    var val;
                    $.each(json[subKey], function(n, obj) {
                        val = JSONUtil.findKeyInValueHistory(obj, key, value);
                        if (val) {
                            JSONUtil.rtnHistory.push(json);
                            return json;
                        }
                    });
                    if (val) {
                        return json;
                    }
                } else if ($.type(json[subKey]) == "object") {
                    if (JSONUtil.findKeyInValueHistory(json[subKey], key, value)) {
                        JSONUtil.rtnHistory.push(json);
                        return json;
                    }
                }
            }
        }
    }
};

// yyyy-mm-dd
function formatDate(date) {
    return $.datepicker.formatDate("yy-mm-dd", date);
}
//yyyy-mm-dd hh:mi
function formatTime(date) {
    return formatDate(date) + " " + lpad(date.getHours(), 2, "0") + ":" + lpad(date.getMinutes(), 2, "0");
}
//yyyy-mm-dd hh:mi:ss
function formatTimeSeconds(date) {
    return formatTime(date) + ":" + lpad(date.getSeconds(), 2, "0");
}

// 현지시간변환(from UTC / to GMT)
// vRetrunType : onlyDate , withDateTime
function setLocalTimeFromUTC(companyId, vUtcTimeStamp, vRetrunType) {
    //var vUtcTimeStamp = "2019-06-07T23:47:52.068Z";

    // 우리나라 일시로 변환
    var vUtcDate = new Date(vUtcTimeStamp);

    /*
    // 밀리세컨드로 바꾸고, 현재 우리나라의 Offset을 더해서 UTC 시간으로 변환
    //var vUtcTime = vUtcDate.getTime() + (vUtcDate.getTimezoneOffset() * 60000);

    var timeOffset = 9;
    // 베트남의 Offset은 GMT+7시간
    if(companyId == '00001') {
        var timeOffset = 7;
    }

    // 다시 세팅
    var convertTime = new Date(vUtcTime + (3600000 * timeOffset));
    //alert(" korea : " + vUtcDate + "\n convertTime : " + convertTime);
    */
    var convertTime = vUtcDate;

    var dateYear = convertTime.getFullYear(); // 년
    var dateMonth = convertTime.getMonth() + 1; // 월
    var dateDay = convertTime.getDate(); // 일
    var dateHour = convertTime.getHours(); // 시
    var dateMinute = convertTime.getMinutes(); // 분
    var dateSecond = convertTime.getSeconds(); // 초

    if (dateMonth.toString().length == 1) {
        dateMonth = "0" + dateMonth;
    }
    if (dateDay.toString().length == 1) {
        dateDay = "0" + dateDay;
    }
    if (dateHour.toString().length == 1) {
        dateHour = "0" + dateHour;
    }
    if (dateMinute.toString().length == 1) {
        dateMinute = "0" + dateMinute;
    }
    if (dateSecond.toString().length == 1) {
        dateSecond = "0" + dateSecond;
    }

    var setLocalTime = "";

    if (vRetrunType == "onlyDate") {
        setLocalTime = dateYear + "-" + dateMonth + "-" + dateDay;
    } else if (vRetrunType == "withDateTime") {
        setLocalTime = dateYear + "-" + dateMonth + "-" + dateDay + " " + dateHour + ":" + dateMinute + ":" + dateSecond;
    }

    //alert(" companyId : " + companyId + "\n setLocalTime : " + setLocalTime);

    return setLocalTime;
}

function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + exdays * 24 * 60 * 60 * 1000);
    var expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}
function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(";");
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == " ") {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

// IMEI Validation
function isIMEI(s) {
    var etal = /^[0-9]{15}$/;
    if (!etal.test(s)) {
        return false;
    }
    sum = 0;
    mul = 2;
    l = 14;
    for (i = 0; i < l; i++) {
        digit = s.substring(l - i - 1, l - i);
        tp = parseInt(digit, 10) * mul;
        if (tp >= 10) {
            sum += (tp % 10) + 1;
        } else {
            sum += tp;
        }
        if (mul == 1) {
            mul++;
        } else {
            mul--;
        }
    }
    chk = (10 - (sum % 10)) % 10;
    if (chk != parseInt(s.substring(14, 15), 10)) return false;
    return true;
}

var $root = $("html, body");

/**
 * jQuery 확장
 */
(function($) {
    $.extend($.fn, {
        populate: function(jsonObj) {
            var $form = $(this);
            $form[0].reset();
            $form.find("textarea").html("");

            $.each(jsonObj, function(key, value) {
                $form.find("input[id=" + key + "], select[id=" + key + "]").not(":radio").val(value);
                $form.find("textarea[id=" + key + "]").html(value);
                if ($form.find("input[id=" + key + "]:radio").length > 0) {
                    var radioList = $form.find("input[id=" + key + "]:radio");
                    $.each(radioList, function(n, radio) {
                        if ($(radio).val() == value) {
                            $(radio).prop("checked", true).change();
                        }
                    });
                }
            });
        },
        serializeForm: function() {
            var result = {};
            var $form = $(this);
            var array = $form.serializeArray();
            $.each(array, function(n, obj) {
                // 배열인 경우
                var key = obj["name"];
                var val = obj["value"];
                if (result[key]) {
                    var tmpObj = result[key];
                    if ($.type(result[key]) != "array") {
                        result[key] = [];
                        result[key].push(tmpObj);
                    }
                    result[key].push(val);
                } else {
                    result[key] = val;
                }
            });
            return result;
        }
    });
})(jQuery);

String.prototype.toDate = function(format) {
    var normalized = this.replace(/[^a-zA-Z0-9]/g, "-");
    var normalizedFormat = format.toLowerCase().replace(/[^a-zA-Z0-9]/g, "-");

    var monthIndex = normalizedFormat.indexOf("mm");
    var dayIndex = normalizedFormat.indexOf("dd");
    var yearIndex = normalizedFormat.indexOf("yyyy");
    var hourIndex = normalizedFormat.indexOf("hh");
    var minutesIndex = normalizedFormat.indexOf("ii");
    var secondsIndex = normalizedFormat.indexOf("ss");

    var today = new Date();
    var year = yearIndex > -1 ? this.substr(yearIndex, 4) : today.getFullYear();
    var month = monthIndex > -1 ? this.substr(monthIndex, 2) - 1 : today.getMonth() - 1;
    var day = dayIndex > -1 ? this.substr(dayIndex, 2) : today.getDate();

    var hour = hourIndex > -1 ? this.substr(hourIndex, 2) : today.getHours();
    var minute = minutesIndex > -1 ? this.substr(minutesIndex, 2) : today.getMinutes();
    var second = secondsIndex > -1 ? this.substr(secondsIndex, 2) : today.getSeconds();

    return new Date(year, month, day, hour, minute, second);
};

/**
 * Undefined 체크
 *
 * @param obj
 * @returns {Blooean}
 */
function checkUndefined(obj) {
    return obj === void 0;
}

/**
 * Undefined To ""
 *
 * @param obj
 * @returns {obj or ""}
 */
function convertUndefinedToString(obj, rtn) {
	if(checkUndefined(obj)){
		if(checkUndefined(rtn)){
			return "";
		} else {
			return rtn;
		}
	} else {
		return obj;
	}
}

/**
 * utf8 문자를 byte 기준으로 substring
 *
 * @param str 대상 문자
 * @param startInBytes 시작byte로 0부터 시작
 * @param lengthInBytes 시작 byte로 lengthInBytes만큼 문자를 가져옴
 * @returns {Blooean}
 */
function substr_utf8_bytes(str, startInBytes, lengthInBytes) {
    /* this function scans a multibyte string and returns a substring.
        * arguments are start position and length, both defined in bytes.
        *
        * this is tricky, because javascript only allows character level
        * and not byte level access on strings. Also, all strings are stored
        * in utf-16 internally - so we need to convert characters to utf-8
        * to detect their length in utf-8 encoding.
        *
        * the startInBytes and lengthInBytes parameters are based on byte
        * positions in a utf-8 encoded string.
        * in utf-8, for example:
        *       "a" is 1 byte,
                "ü" is 2 byte,
           and  "你" is 3 byte.
        *
        * NOTE:
        * according to ECMAScript 262 all strings are stored as a sequence
        * of 16-bit characters. so we need a encode_utf8() function to safely
        * detect the length our character would have in a utf8 representation.
        *
        * http://www.ecma-international.org/publications/files/ecma-st/ECMA-262.pdf
        * see "4.3.16 String Value":
        * > Although each value usually represents a single 16-bit unit of
        * > UTF-16 text, the language does not place any restrictions or
        * > requirements on the values except that they be 16-bit unsigned
        * > integers.
        */

    var resultStr = "";
    var startInChars = 0;

    // scan string forward to find index of first character
    // (convert start position in byte to start position in characters)

    for (bytePos = 0; bytePos < startInBytes; startInChars++) {
        // get numeric code of character (is >128 for multibyte character)
        // and increase "bytePos" for each byte of the character sequence
        ch = str.charCodeAt(startInChars);
        if (isNaN(ch)) {
            break;
        }
        bytePos += (ch < 128) ? 1 : utf8.encode(str[startInChars]).length;
    }

    // now that we have the position of the starting character,
    // we can built the resulting substring

    // as we don't know the end position in chars yet, we start with a mix of
    // chars and bytes. we decrease "end" by the byte count of each selected
    // character to end up in the right position
    end = startInChars + lengthInBytes - 1;

    // startInChars의 조건이 <= 일 경우 마지막 글자의 바이트가 일부만 포함해도 표시, < 경우는 전부 포함되어야 표시
    for (n = startInChars; startInChars < end; n++) {
        // get numeric code of character (is >128 for multibyte character)
        // and decrease "end" for each byte of the character sequence

        ch = str.charCodeAt(n);
        if (str[n] == undefined) {
            break;
        }
        end -= (ch < 128) ? 1 : utf8.encode(str[n]).length;

        resultStr += str[n];
    }

    return resultStr;
}

/**
 * bytes of a string
 *
 * @param String
 * @returns int
 */
function getByte(s){
    if (s != undefined && s != "") {
        for (b = i = 0; (c = s.charCodeAt(i++)); b += c >> 7 ? 2 : 1);
        return b;
    } else {
        return 0;
    }
}

/**
 * UTF-8 bytes of a string
 *
 * @param String
 * @returns int
 */
function getByteUtf8(s) {
    if (s != undefined && s != "") {
        for (b = i = 0; (c = s.charCodeAt(i++)); b += c >> 11 ? 3 : c >> 7 ? 2 : 1);
        return b;
    } else {
        return 0;
    }
}

/**
 * 조회조건 날짜 설정
 *
 * @param 시작일자id, 종료일자id, 시작일자, 종료일자, 일자인터벌
 */
function setSearchPeriod(startId, endId, startDate, endDate, inDay) {
    if (inDay) {
        startDate.setDate(endDate.getDate() - inDay);
    }

    var start = $("#" + startId)
        .datepicker()
        .datepicker("setDate", startDate)
        .datepicker("option", "maxDate", endDate)
        .on("change", function() {
            end.datepicker("option", "minDate", this.value.toDate("yyyy-mm-dd"));
        });

    var end = $("#" + endId)
        .datepicker()
        .datepicker("setDate", endDate)
        .datepicker("option", "maxDate", endDate)
        .on("change", function() {
            start.datepicker("option", "maxDate", this.value.toDate("yyyy-mm-dd"));
        });
}

/**
 * 날짜 유효성 체크
 *
 * @param String yyyymmdd
 * @return boolean
 */
function isValidDate(inYmd) {
    var year = inYmd.substring(0, 4);
    var month = inYmd.substring(4, 6);
    var day = inYmd.substring(6, 8);

    var d = new Date(year, month - 1, day);

    if (d.getFullYear() == year && lpad(d.getMonth() + 1, 2, "0") == month && lpad(d.getDate(), 2, "0") == day) {
        return true;
    }

    return false;
}

/**
 * (from ~ to) 일수 조회
 *
 * @param String yyyy-mm-dd
 * @return boolean
 */
function getDayDiff(startYmd, endYmd) {
    var startYmdArray = startYmd.split("-");
    var endYmdArray = endYmd.split("-");

    var startDate = new Date(startYmdArray[0], Number(startYmdArray[1])-1, startYmdArray[2]);
    var endDate = new Date(endYmdArray[0], Number(endYmdArray[1])-1, endYmdArray[2]);

    var dayDiff = (endDate - startDate)/60/60/24/1000 + 1;
    return dayDiff;
};

/**
 * 숫자여부 체크
 *
 * @param Number
 * @return boolean
 */
function isNumeric(num, opt) {
    // 좌우 trim(공백제거)을 해준다.
    num = String(num).replace(/^\s+|\s+$/g, "");

    if (typeof opt == "undefined" || opt == "1") {
        // 모든 10진수 (부호 선택, 자릿수구분기호 선택, 소수점 선택)
        var regex = /^[+\-]?(([1-9][0-9]{0,2}(,[0-9]{3})*)|[0-9]+){1}(\.[0-9]+)?$/g;
    } else if (opt == "2") {
        // 부호 미사용, 자릿수구분기호 선택, 소수점 선택
        var regex = /^(([1-9][0-9]{0,2}(,[0-9]{3})*)|[0-9]+){1}(\.[0-9]+)?$/g;
    } else if (opt == "3") {
        // 부호 미사용, 자릿수구분기호 미사용, 소수점 선택
        var regex = /^[0-9]+(\.[0-9]+)?$/g;
    } else {
        // only 숫자만(부호 미사용, 자릿수구분기호 미사용, 소수점 미사용)
        var regex = /^[0-9]$/g;
    }

    if (regex.test(num)) {
        num = num.replace(/,/g, "");
        return isNaN(num) ? false : true;
    } else {
        return false;
    }
}

//트리 변환 메서드
function treeModel(arrayList, rootId) {
    var rootNodes = [];
    var traverse = function(nodes, item, index) {
        if (nodes instanceof Array) {
            return nodes.some(function(node) {
                if (node.id === item.parentId) {
                    node.children = node.children || [];
                    return node.children.push(arrayList.splice(index, 1)[0]);
                }

                return traverse(node.children, item, index);
            });
        }
    };

    while (arrayList.length > 0) {
        arrayList.some(function(item, index) {
            if (item.parentId === rootId) {
                return rootNodes.push(arrayList.splice(index, 1)[0]);
            }

            return traverse(rootNodes, item, index);
        });
    }

    return rootNodes;
}

/**
 * 배열내 특정 데이터 건수
 *
 * @param Array arr, String str
 * @return boolean
 */
function strCountInArray(arr, str) {
    var cnt = 0;

    if (Array.isArray(arr)) {
        // arr instanceof Array 로 대체 가능
        cnt = arr.filter(val => {
            return val == str;
        }).length; // 화살표함수 대체 function(val){return val==str}
    } else {
        cnt = 0;
    }

    return cnt;
}

/**
 * 기간 중복 여부
 *
 * @param String fromYmd, String toYmd, String tarFromYmd, String tarToYmd
 * @return String rtn
 */
function isPeriodDuplicationDate(fromYmd, toYmd, tarFromYmd, tarToYmd) {
    var rtn = "";

    // 기간 중복 여부
    if (isValidDate(fromYmd) && isValidDate(toYmd) && isValidDate(tarFromYmd) && isValidDate(tarToYmd)) {
        if (fromYmd > toYmd) {
            rtn = "The fromYmd is greater than the toYmd.";
        } else if (tarFromYmd > tarToYmd) {
            rtn = "The tarFromYmd is greater than the tarToYmd.";
        } else {
            if (tarFromYmd > toYmd || tarToYmd < toYmd) {
                rtn = "N"; // 중복되지 않음
            } else {
                rtn = "Y";
            }
        }
    } else {
        rtn = "Input data is not valid.";
    }

    return rtn;
}

/**
 * 숫자 구간 중복 여부
 *
 * @param int fromNum, int toNum, int tarFromNum, int tarToNum, numOpt
 * @return String rtn
 */
function isPeriodDuplicationNumeric(fromNum, toNum, tarFromNum, tarToNum, numOpt, isEqual ) {
    var rtn = "";

    // 기간 중복 여부
    if(isNumeric(fromNum, numOpt) && isNumeric(toNum, numOpt) && isNumeric(tarFromNum, numOpt) && isNumeric(tarToNum, numOpt)) {
        if(fromNum > toNum) {
            rtn = "The fromNumber is greater than the toNumber.";
        } else if (tarFromNum > tarToNum) {
            rtn = "The tarFromNumber is greater than the tarToNumber.";
        } else {
            if(isEqual == "Y") {
                if(tarFromNum >= toNum || tarToNum <= fromNum) {
                    rtn = "N"; // 중복되지 않음
                } else {
                    rtn = "Y";
                }
            } else {
                if(tarFromNum > toNum || tarToNum < fromNum) {
                    rtn = "N"; // 중복되지 않음
                } else {
                    rtn = "Y";
                }
            }
        }
    } else {
        rtn = "Input data is not valid.";
    }

    return rtn;
}

//콤마찍기
function numberWithCommas(x) {
    var strCommas = "";

    if (x != undefined && x != "") {
        strCommas = x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }
    return strCommas;
}

//콤마찍기
function comma(str) {
    var strCommas = "";

    if (str != undefined && str != "") {
        strCommas = String(str);
        strCommas = strCommas.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, "$1,");
    }
    return strCommas;
}

//콤마풀기
function uncomma(str) {
    str = String(str);
    return str.replace(/[^\d]+/g, "");
}

// 시간 날짜형식 유효성 체크
function insertTimeCheck(strValue) {
    if(strValue.substring(0,2) > 24) {
        // alert("시간을 다시 입력하세요.");
        return false;
    }else if(strValue.substring(2,4) > 60) {
        // alert("분을 다시 입력하세요.");
        return false;
    }else{
        return true;
    }
}

function fnDatasetMaxByteCheck(dataSet, colArray, encodingType){
    let msg = "";

    if (colArray == undefined || colArray == null) {
        msg = "No check information.";
        return msg;
    }

    for(var i=0; i<colArray.length; i++){
        let chkRows = new Array();
        let col = colArray[i].col;
        let colname = colArray[i].colname;
        let maxByte = colArray[i].maxByte;

        for(var j=0; j<dataSet.length;j++){
            let dataInfo = dataSet[j];
            let dataByte = 0;
            // byte값을 계산하는 펑션을 이용하요 byte값을 계산하여 비교
            if(encodingType!=undefined && encodingType.toUpperCase()=="UTF-8"){
                dataByte = getByteUtf8(dataInfo[col]);
            } else {
                dataByte = getByte(dataInfo[col]);
            }

            if(dataByte > maxByte) {
              chkRows.push(j+1);
            };
        }

        if(chkRows.length > 0) {
            msg = msg + colname + " in line " + "[" + chkRows + "] "+ "exceeds " + maxByte + " Byte.\n";
        }
    }

    return msg;
}


//형식 체크 type 체크
function validationCheck(type, val, opt) {

    var regexp = "";
    var errMessage = "";

 if(type=="1") {  // 대문자, 숫자만 허용
    regexp = /^[A-Z0-9+]*$/;
 }

 if( !regexp.test(val) ) {
        return false;
 }

 return true;
};

//두개의 날짜를 비교하여 차이를 알려준다.
function dateDiff(_date1, _date2) {
    var diffDate_1 = _date1 instanceof Date ? _date1 : new Date(_date1);
    var diffDate_2 = _date2 instanceof Date ? _date2 : new Date(_date2);

    diffDate_1 = new Date(diffDate_1.getFullYear(), diffDate_1.getMonth()+1, diffDate_1.getDate());
    diffDate_2 = new Date(diffDate_2.getFullYear(), diffDate_2.getMonth()+1, diffDate_2.getDate());

    var diff = Math.abs(diffDate_2.getTime() - diffDate_1.getTime());
    diff = Math.ceil(diff / (1000 * 3600 * 24));

    return diff;
}

//오늘날자를 리턴한다.
function getTodayYmd(s) {

    var now = new Date();

    var dateYy = now.getFullYear(); // 년
    var dateMm = now.getMonth()+1;  // 월
    var dateDd = now.getDate();     // 일

    var dateTodayYmd = "";

    if ( dateMm.toString().length == 1 ) {
        dateMm = "0" + dateMm ;
    }

    if ( dateDd.toString().length == 1 ) {
        dateDd = "0" + dateDd ;
    }

    var ds = "";

    if(s){
        ds = s;
    }

    dateTodayYmd = dateYy + ds + dateMm + ds + dateDd;

    return dateTodayYmd;
}

//현재 Date값을 리턴한다.
function getTodayYmdhms(s) {

    var now = new Date();

    var dateYear   = now.getFullYear();   // 년
    var dateMonth  = now.getMonth()+1;    // 월
    var dateDay    = now.getDate();       // 일
    var dateHour   = now.getHours();      // 시
    var dateMinute = now.getMinutes();    // 분
    var dateSecond = now.getSeconds();    // 초

    var Ymdhms = "";

    if(dateMonth < 10) {
        dateMonth = "0" + dateMonth ;
    }
    if(dateDay < 10) {
        dateDay = "0" + dateDay ;
    }
    if(dateHour < 10) {
        dateHour = "0" + dateHour ;
    }
    if(dateMinute < 10) {
        dateMinute = "0" + dateMinute ;
    }
    if(dateSecond < 10) {
        dateSecond = "0" + dateSecond ;
    }

    var ds = "";

    if(s){
        ds = s;
    }

    Ymdhms = dateYear + ds + dateMonth + ds + dateDay + dateHour + ds + dateMinute + ds + dateSecond;

    return Ymdhms;
}

function getUtcTodayYmd(s) {

    var now = new Date();

    var _utc = new Date(now.getTime() + (now.getTimezoneOffset() * 60000));  // UTC 시간으로 변환

    var dateYy = _utc.getFullYear();    // 년
    var dateMm = _utc.getMonth()+1;     // 월
    var dateDd = _utc.getDate();        // 일

    var dateUtcTodayYmd = "";

    if ( dateMm.toString().length == 1 ) {
        dateMm = "0" + dateMm ;
    }

    if ( dateDd.toString().length == 1 ) {
        dateDd = "0" + dateDd ;
    }

    var ds = "";

    if(s){
        ds = s;
    }

    dateUtcTodayYmd = dateYy + ds + dateMm + ds + dateDd;

    return dateUtcTodayYmd;
}

/* IE 미지원하여 주석처리 함
class TableCSVExporter {
    constructor (table, includeHeaders = true) {
        this.table = table;
        this.rows = Array.from(table.querySelectorAll("tr"));

        if (!includeHeaders && this.rows[0].querySelectorAll("th").length) {
            this.rows.shift();
        }
    }

    convertToCSV () {
        const lines = [];
        const numCols = this._findLongestRowLength();

        for (const row of this.rows) {
            let line = "";

            for (let i = 0; i < numCols; i++) {
                if (row.children[i] !== undefined) {
                    line += TableCSVExporter.parseCell(row.children[i]);
                }

                line += (i !== (numCols - 1)) ? "," : "";
            }

            lines.push(line);
        }

        return lines.join("\n");
    }

    _findLongestRowLength () {
        return this.rows.reduce((l, row) => row.childElementCount > l ? row.childElementCount : l, 0);
    }

    static parseCell (tableCell) {
        let parsedValue = tableCell.textContent;

        // Replace all double quotes with two double quotes
        parsedValue = parsedValue.replace(/"/g, `""`);

        // If value contains comma, new-line or double-quote, enclose in double quotes
        parsedValue = /[",\n]/.test(parsedValue) ? `"${parsedValue}"` : parsedValue;

        return parsedValue;
    }
}
*/

function exportTableToCSV($table, filename) {
    var $headers = $table.find('tr:has(th)')
        ,$rows = $table.find('tr:has(td)')
        // Temporary delimiter characters unlikely to be typed by keyboard
        // This is to avoid accidentally splitting the actual contents
        ,tmpColDelim = String.fromCharCode(11) // vertical tab character
        ,tmpRowDelim = String.fromCharCode(0) // null character

        // actual delimiter characters for CSV format
        ,colDelim = '","'
        ,rowDelim = '"\r\n"';

        // Grab text from table into CSV formatted string
    var csv = '"';
    csv += formatRows($headers.map(grabRow));
    csv += rowDelim;
    csv += formatRows($rows.map(grabRow)) + '"';
    // Data URI
    var csvData = 'data:application/csv;charset=utf-8,' + encodeURIComponent(csv);

    // For IE (tested 10+)
    if (window.navigator.msSaveOrOpenBlob) {
        var blob = new Blob(["\ufeff"+decodeURIComponent(encodeURI(csv))], {
            type: "text/csv;charset=utf-8;"
        });
        navigator.msSaveBlob(blob, filename);
    } else {
        const csvBlob = new Blob(["\ufeff"+csv], {type: "text/csv;charset=utf-8;"});
        const blobUrl = URL.createObjectURL(csvBlob);
        const anchorElement = document.createElement("a");

        anchorElement.href = blobUrl;
        anchorElement.download = filename;
        anchorElement.click();
    }

    //------------------------------------------------------------
    // Helper Functions 
    //------------------------------------------------------------
    // Format the output so it has the appropriate delimiters
    function formatRows(rows){
        return rows.get().join(tmpRowDelim)
            .split(tmpRowDelim).join(rowDelim)
            .split(tmpColDelim).join(colDelim);
    }
    // Grab and format a row from the table
    function grabRow(i,row){
         
        var $row = $(row);
        //for some reason $cols = $row.find('td') || $row.find('th') won't work...
        var $cols = $row.find('td'); 
        if(!$cols.length) $cols = $row.find('th');  

        return $cols.map(grabCol)
                    .get().join(tmpColDelim);
    }
    // Grab and format a column from the table 
    function grabCol(j,col){
        var $col = $(col),
            $text = $col.text();

        return $text.replace('"', '""'); // escape double quotes
    }
}