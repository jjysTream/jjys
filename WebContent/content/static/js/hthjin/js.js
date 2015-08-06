
/*背景色改变*/
$(function(){
	   $('.indivters_all ul:odd').css("backgroundColor","#f2f2f2");
	   //获取id为tb的元素,然后寻找他下免的tbody标签，再寻找tbody下索引值是偶数的tr元素,
	   //改变它的背景色.
	});
	
	
$(function(){
	//商品分类
	$('ul.clt>li').hover(function(){
		$(this).addClass('active');
		$(this).find('.navslov').show();
	},function(){
		$(this).removeClass('active');
		$(this).find('.navslov').hide();
	});
});

/***************************************可取消*****************************************/
$(document).ready(function(){
	$('.allclassify li a').click(function(){
        $(this).addClass('allclassify_on').siblings().removeClass('allclassify_on');
        })
});
/***************************************mask*****************************************/
$(document).ready(function() {
	
	$('.mask').css('display','block');
});

$(document).ready(function(){
	$('.allmain_btn_on').click(function(){
        $('.mask').css('display','none');
        })
});
/***************************************indt_edito*****************************************/
$(document).ready(function() {
	
	$('.indt_edito').css('display','none');
});
$(document).ready(function(){
	$('.indivters_onedito').click(function(){
        $('.indt_edito').css('display','block');
        })
});
$(document).ready(function(){
	$('.indt_edito_on').click(function(){
        $('.indt_edito').css('display','none');
        })
});
/***************************************indt_edito*****************************************/
$(document).ready(function() {
	
	$('.shade').css('display','none');
});
$(document).ready(function(){
	$('.morr').click(function(){
        $('.shade').css('display','block');
		var dddd = $(this).html();
		$('.shade_edito').html(dddd);
        });
	$('.shade_on').click(function(){
        $('.shade').css('display','none');
        })
	$('.allmain').click(function(){
        $('.shade').css('display','none');
        })
});

$(function() {
            var div1 = $(".indivshop_left");
            var div2 = $(".indivshop_right");

            var h = GetMaxHeight(div1, div2);
            div1.css("height", h);
            div2.css("height", h);
            
        });


        function GetMaxHeight(div1, div2) {
            var h1 = $(div1).attr("offsetHeight");
            var h2 = $(div2).attr("offsetHeight");

            return Math.max(h1, h2);
        }


// JavaScript Document
$(function(){
    $("a.procat_til").click(function(){                          
	$(this).next("div").slideToggle("normal"); 
	$(this).parent("li").siblings(".procat").children("div").slideUp("normal");});
})
$(function(){
		var $toggleBtn = $('a.proclist_til_onc');             //  获取“显示全部品牌”按钮
		$toggleBtn.click(function(){
		      if($(this).siblings("div").hasClass("proclist_none")){
					               		 //  隐藏$category
					$(this)     
						.text("收起▲");                  //改变背景图片和文本
					$(this).siblings("div").removeClass("proclist_none");			// 去掉高亮样式
			  }else{
					                		 //  显示$category
					$(this)   
						.text("展开▼");                  //改变背景图片和文本
					$(this).siblings("div").addClass("proclist_none");//添加高亮样式
			  }
			return false;					      	//超链接不跳转
		})
})





/*******************************TAB切换***********************************/

function tabSlider(obj,objs,objsn){
	$(function(){
	$(obj + ':first').addClass('cata_til_onck');
	$(objsn + ':first').css('display','block');
		autoroll();
		hookThumb();
	})
	 var i=-1;//第i+1个tab开始
		
	function autoroll(){
		nca = $(obj).length-1;
		i++;
		if(i > nca){
		i = 0;
		}
		slide(i);
	}
	
	function slide(i){
		$(obj).eq(i).addClass(objs).siblings().removeClass(objs);
		$(objsn).eq(i).css('display','block').siblings(objsn).css('display','none');
	}
	
	function hookThumb(){    
		$(obj).hover(
		function(){
				i = $(this).prevAll().length;
				slide(i); 
		},function(){
			this.blur();            
			return false;
		}); 
	}
}


function tabSliderclock(obj,objs,objsn){
	$(function(){
		$(obj + ':first').addClass('cata_til_onck');
		$(objsn + ':first').css('display','block');
		autoroll();
		hookThumb();
	})
	 var i=-1;
		
	function autoroll(){
		nca = $(obj).length-1;
		i++;
		if(i > nca){
		i = 0;
		}
		slide(i);
	}
	
	function slide(i){
		$(obj).eq(i).addClass(objs).siblings().removeClass(objs);
		$(objsn).eq(i).css('display','block').siblings(objsn).css('display','none');
	}
	
	function hookThumb(){    
		$(obj).click(
		function(){
				i = $(this).prevAll().length;
				slide(i); 
		},function(){
			this.blur();            
			return false;
		}); 
	}
}
//tab开始
$(document).ready(function(){
	tabSlider(".cata_til li","cata_til_onck",".cata_prod");
	tabSlider(".news_til li","news_til_onck",".news_prod");
	tabSliderclock(".recharge_til li","recharge_til_onck",".recharge_prod");
	tabSlider(".conts_til li","conts_til_onck",".conts_prod");
	tabSlider(".brand_til li","brand_til_onck",".brand_prod");
	tabSliderclock(".produta_deta_til li","produtadtt_omc",".produta_deta_all");
})










/************************************************************/

$(document).ready(function (){
	
	//点击小图切换大图
	$("#thumbnail li a").click(function(){
		$(".zoompic img").hide().attr({ "src": $(this).attr("href"), "title": $("> img", this).attr("title") });
		$("#thumbnail li.current").removeClass("current");
		$(this).parents("li").addClass("current");
		return false;
	});
	$(".zoompic>img").load(function(){
		$(".zoompic>img:hidden").show();
	});
	
	//小图片左右滚动
	var $slider = $('.slider ul');
	var $slider_child_l = $('.slider ul li').length;
	var $slider_width = $('.slider ul li').width() + 2;
	$slider.width($slider_child_l * $slider_width);
	
	var slider_count = 0;
	
	if ($slider_child_l < 7) {
		$('#btn-right').css({cursor: 'auto'});
		$('#btn-right').removeClass("dasabled");
	}
	
	$('#btn-right').click(function() {
		if ($slider_child_l < 7 || slider_count >= $slider_child_l - 7) {
			return false;
		}
		
		slider_count++;
		$slider.animate({left: '-=' + $slider_width + 'px'}, 'fast');
		slider_pic();
	});
	
	$('#btn-left').click(function() {
		if (slider_count <= 0) {
			return false;
		}
		slider_count--;
		$slider.animate({left: '+=' + $slider_width + 'px'}, 'fast');
		slider_pic();
	});
	
	function slider_pic() {
		if (slider_count >= $slider_child_l - 7) {
			$('#btn-right').css({cursor: 'auto'});
			$('#btn-right').addClass("dasabled");
		}
		else if (slider_count > 0 && slider_count <= $slider_child_l - 7) {
			$('#btn-left').css({cursor: 'pointer'});
			$('#btn-left').removeClass("dasabled");
			$('#btn-right').css({cursor: 'pointer'});
			$('#btn-right').removeClass("dasabled");
		}
		else if (slider_count <= 0) {
			$('#btn-left').css({cursor: 'auto'});
			$('#btn-left').addClass("dasabled");
		}
	}
	
});






















