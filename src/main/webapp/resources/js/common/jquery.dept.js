(function($) {
    $.extend($.fn, {
        // url : _CONTEXT_PATH + 'common/selectCenterList.json',
        dept : function(selectedDeptCd, boxWidth, isSingle, isOptGroup) {
            this.url = _CONTEXT_PATH + 'common/selectCenterList.json';
            return this._generate(selectedDeptCd, boxWidth, isSingle, isOptGroup);
        },
        deptAll : function(selectedDeptCd, boxWidth, isSingle, isOptGroup, isCheckAll) {
            this.url = _CONTEXT_PATH + 'common/selectDeptList.json';
            return this._generate(selectedDeptCd, boxWidth, isSingle, isOptGroup, isCheckAll);
        },
        _generate : function(selectedDeptCd, boxWidth, isSingle, isOptGroup, isCheckAll) {
            var _this = this;
            var req = ajax.json(this.url,
            {searchParent : 'Y'},
            function(res) {
                if (res) {
                    var optgroupList = res.filter(function(obj) {
                        return obj.deptLevel == 1;
                    });
                    $.each(optgroupList, function(n, optgroup) {
                        $('<optgroup>').attr('label', optgroup.deptNmA)
                            .data('deptCd', optgroup.deptCd)
                            .appendTo(_this);
                    });

                    $('optgroup', _this).each(function(n,   optgroup) {
                        var pDeptCd = $(this).data('deptCd');
                        var optionList = res.filter(function(obj) {
                            return pDeptCd == obj.parentDeptCd;
                        });
                        $.each(optionList, function(nn, opt) {
                            $('<option>').val(opt.deptCd)
                                .text(opt.deptNmA)
                                .data('parentDeptCd',opt.parentDeptCd)
                                .data('deptLevel', opt.deptLevel)
                                .data('shipToNo', opt.shipToNo)
                                .appendTo(optgroup);
                        });
                    })
                }
            });

            req.done(function() {
                $(_this).multipleSelect({
                    isopen : true,
                    multiple : true,
                    multipleWidth : 150,
                    dropWidth : 300,
                    single : isSingle == true ? true : false,
                    width : boxWidth ? boxWidth : 950,
                    useOptGroup : isOptGroup == true ? true : false,
                    placeholder : '충전소를 선택하여 주십시오.'
                });

                if (isCheckAll){
                	$(_this).multipleSelect("checkAll");                	
                } else {
                	if ($.type(selectedDeptCd) == 'string') {
                		$(_this).multipleSelect("setSelects", [ selectedDeptCd ]);
                	} else if ($.type(selectedDeptCd) == 'array') {
                		$(_this).multipleSelect("setSelects", selectedDeptCd);
                	}
                }
            })

            return req;
        }
    });
})(jQuery);
