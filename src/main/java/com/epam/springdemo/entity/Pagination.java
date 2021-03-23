package com.luv2code.springdemo.entity;

public class Pagination {

	public int currentPage;
	
	public int limit=6;
	
	public double total;
	
	public int amount;
	
	int catalog;
	
	public String getHtml(){
		
		final char dm = (char) 34;
		String htmlText = "<ul class="+dm+"pagination"+dm+">";
		int limits [] = limits();
		
		for(int i =limits[0];i<=limits[1];i++) {
			
			if(i==currentPage){
				htmlText+="<li class="+dm+"active"+dm+"><a href="+dm+"#"+dm+">"+ i + "</a></li>";
			}
			else {
				htmlText+="<li><a href="+dm+"/web-customer-tracker/catalog/category/"+
						catalog+"/page-"+i+dm+">"+i+"</a></li>";
			}
		}
		htmlText+="</ul>";
		return htmlText;
	}

	public Pagination(double total, int currentPage,int catalog) {
		this.catalog=catalog;
		this.currentPage=currentPage;
		this.total=total;
		this.amount=amount();
		setCurrentPage(currentPage);
	}
	
	
   public int amount() {
	   return (int)Math.ceil(total/limit);
   }
   
   
   public void setCurrentPage(int currentPage){
       
	  this.currentPage = currentPage;

      if(this.currentPage>0) {
    	  
    	  if (this.currentPage > this.amount)
                  this.currentPage = this.amount;	  
      }
      else 
    	  this.currentPage=1;  
   }
   
   private int [] limits(){
	   
	   int left = this.currentPage -5;
	   
	   int start = left>0?left:0;
	   int end;
	   
	   if (start + 10 <= this.amount)
	            end = start > 1 ? start + 10 : 10;
	        else {
	            end = amount;
	            start = amount - 10> 0 ? amount - 10: 1;
	        }
	   
	   return new int[] {start,end};  
	   
   }
   
   
   
}
