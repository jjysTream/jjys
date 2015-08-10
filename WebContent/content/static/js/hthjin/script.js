
$(function(){
	
	//init kv and teasers
	(function(){
		var $kv = jQuery('.js_nf_hp_kv')
		var $kv_window = $kv.find('.js_window');
		var $kv_items = $kv.find('.kv_item')
		var $kv_nav = $kv.find('.js_nav');
		var $kv_prev = $kv.find('.js_btn_prev');
		var $kv_next = $kv.find('.js_btn_next');
		var kv_items_count = $kv_items.length;
		
		var $ts = jQuery('.js_nf_hp_teaser_c3x')
		var $ts_groups = $ts.find('.js_t_group');
		
		var animate_dur = 500;
		var play_dur = 2000;
		
		var stop_flag = false;
		
		var $window = jQuery(window);
		
		var cur_idx = 0;
		
		

				
		$kv_items.hide();
		$ts_groups.find('.t_item').hide();
		var goto = function( idx ){
			
			cur_idx = idx;
			
			//idx from 0
			var $kv_visible = $kv_items.filter(':visible');

			if ( $kv_visible.length ){
				$kv_visible.fadeOut(animate_dur);
				$kv_items.eq(idx).fadeIn(animate_dur);
			}else{
				$kv_items.eq(idx).show();
			}
			
			$ts_groups.each(function(g_idx){
				var $ts_group = jQuery(this);
				var $ts_items = $ts_group.find('.t_item');
				var $ts_visible = $ts_items.filter(':visible');

				if ( $ts_visible.length ){
					$ts_visible.css({'z-index':99});
					
					window.setTimeout(function(){
						$ts_items.eq(idx).css({'top':'158px','z-index':100}).show().animate({'top':0},animate_dur,function(){
							$ts_visible.hide();
						});
					}, 80*g_idx );
					
				}else{
					$ts_items.eq(idx).show();
				}
			});
			
			$kv_nav.find('li').eq(idx).addClass('active').siblings().removeClass('active');
			
		}
		
		//init $kv_prev $kv_next
		$kv_prev.click(function(){
			var next_idx = cur_idx - 1;
			if ( next_idx < 0 ){
				next_idx = kv_items_count - 1;
			}
			goto( next_idx );
		});

		$kv_next.click(function(){
			var next_idx = cur_idx + 1;
			if ( next_idx >= kv_items_count ){
				next_idx = 0;
			}
			goto( next_idx );
		});		
		
		//init $nav
		for ( var i=0; i<kv_items_count; i++ ){
			var $li = jQuery('<li><a href="javascript:;">'+ ( i+1 ) +'</a></li>');
			
			(function(){
				var cur_idx = i;
				$li.click(function(){
					goto(cur_idx);
				});
			})();
			
			$kv_nav.append($li);
		}
		$kv_nav.css('margin-left',-(kv_items_count*10));
		
		//init kv_item
		$kv_items.each(function(){
			var $this_kv_item = jQuery(this);
			var this_bg = $this_kv_item.data('bg');
			$this_kv_item.css('background-image','url('+this_bg+')');
		});
		
		//init btn_nav
		var repos_btn_nav = function(){
			var offset = $kv.offset();
		}
		$window.resize(function(){
			repos_btn_nav();
		});
		


		$kv.hover(function(){
			stop_flag = true;
		},function(){
			stop_flag = false;
		});

		$ts.hover(function(){
			stop_flag = true;
		},function(){
			stop_flag = false;
		});	
		
		window.setInterval(function(){
			if ( !stop_flag ){
				$kv_next.click();
			}
		},play_dur);
		
		
		
		repos_btn_nav();
		goto(cur_idx);
		
	})();

})