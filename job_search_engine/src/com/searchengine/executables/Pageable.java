package com.searchengine.executables;
import java.util.List;


public class Pageable<T> {

  /** the default page size */
  public static final int DEFAULT_PAGE_SIZE = 10;

  private static final int DEFAULT_PAGE_WINDOW = 5;

  /** the list over which this class is paging */
  private List<T> list;

  /** the page size */
  private int pageSize = DEFAULT_PAGE_SIZE;
  
  /** the page window */
  private int pageWindow = DEFAULT_PAGE_WINDOW;

  /** the current page */
  private int page;

  /** the starting index */
  private int startingIndex;

  /** the ending index */
  private int endingIndex;

  /** the maximum number of pages */
  private int maxPages;

  /**
   * Creates a new instance with the specified list.
   *
   * @param list    a List
   */
  public Pageable(List<T> list) {
    this.list = list;
    this.page = 1;
    this.maxPages = 1;

    calculatePages();
  }

  private void calculatePages() {
    if (pageSize > 0) {
      // calculate how many pages there are
      if (list.size() % pageSize == 0) {
        maxPages = list.size() / pageSize;
      } else {
        maxPages = (list.size() / pageSize) + 1;
      }
    }
  }
  
  /*
   * Set methods
   */
  
  /**
   * Sets the page size.
   *
   * @param pageSize   the page size as an int
   */
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
    calculatePages();
  }
  
  /**
   * Set the page window
   * 
   * @param pageWindo the page windo as an int
   */
  public void setPageWindow(int pageWindow){
	  this.pageWindow = pageWindow;
  }
  
  /**
   * Sets the page size.
   *
   * @param p    the page as an int
   */
  public void setPage(int p) {
    if (p >= maxPages) {
      this.page = maxPages;
    } else if (p <= 1) {
      this.page = 1;
    } else {
      this.page = p;
    }

    // now work out where the sub-list should start and end
    startingIndex = pageSize * (page-1);
    if (startingIndex < 0) {
      startingIndex = 0;
    }
    endingIndex = startingIndex + pageSize;
    if (endingIndex > list.size()) {
      endingIndex = list.size();
    }
  }


  /*
   * Get methods
   */

  /**
   * Gets the list that this instance is paging over.
   *
   * @return  a List
   */
  public List<T> getList() {
    return this.list;
  }

  /**
   * Gets the subset of the list for the current page.
   *
   * @return  a List
   */
  public List<T> getListForPage() {
    return list.subList(startingIndex, endingIndex);
  }

  /**
   * Gets the page size.
   *
   * @return  the page size as an int
   */
  public int getPageSize() {
    return this.pageSize;
  }

  /**
   * Gets the page.
   *
   * @return  the page as an int
   */
  public int getPage() {
    return this.page;
  }

  /**
   * Gets the maximum number of pages.
   *
   * @return  the maximum number of pages as an int
   */
  public int getMaxPages() {
    return this.maxPages;
  }

  /**
   * Determines whether there is a previous page and gets the page number.
   *
   * @return  the previous page number, or zero
   */
  public int getPreviousPage() {
    if (page > 1) {
      return page-1;
    } else {
      return 0;
    }
  }

  /**
   * Determines whether there is a next page and gets the page number.
   *
   * @return  the next page number, or 0
   */
  public int getNextPage() {
    if (page < maxPages) {
      return page+1;
    } else {
      return 0;
    }
  }

  /**
   * Gets the minimum page in the window.
   *
   * @return  the page number
   */
  public int getMinPageRange() {
    if (getPage() > pageWindow) {
      return getPage() - pageWindow;
    } else {
      return 1;
    }
  }

  /**
   * Gets the maximum page in the window.
   *
   * @return  the page number
   */
  public int getMaxPageRange() {
    if (getPage() < (getMaxPages() - pageWindow)) {
      return getPage() + pageWindow;
    } else {
      return getMaxPages();
    }
  }

}