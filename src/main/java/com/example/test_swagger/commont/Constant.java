package com.example.test_swagger.commont;

/**
 * 
 * @ClassName: Constant
 * @Description: 常量
 *
 */
public class Constant {

	/**
	 * 
	 * @ClassName: ScheduleStatus
	 * @Description: 定时任务状态
	 * @author lovefamily
	 * @date 2018年7月3日 下午1:54:56
	 *
	 */
	public enum ScheduleStatus {
		/**
		 * 正常
		 */
		NORMAL(0),
		/**
		 * 暂停
		 */
		PAUSE(1);

		private int value;

		private ScheduleStatus(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	public final static String excel2003L = ".xls"; // 2003- 版本的excel
	public final static String excel2007U = ".xlsx"; // 2007+ 版本的excel
	public final static String excelZip = ".zip"; // 2007+ 版本的excel
	public final static String excelPDF = ".pdf"; // 2007+ 版本的excel

	public static final int MAX_RECORD_NUM = 10000;

	public static final int MAX_COUNT_NUM = 1000000;

	public static final String STATUS_SUCCESS = "Success";
	
	public static final String STATUS_PUBLISH = "Publish";
	
	public static final String STATUS_RUNNING = "Running";
	
	public static final String STATUS_ERROR = "Error";
	
	public static final String QUANTITY_DECIMAL = "quantity_decimal"; //设置数量保溜小数
	
	public static final String AMOUNT_DECIMAL = "amount_decimal";   //设置金额保留小数
	
	public static final String MARKUP_CONVERT = "markup_convert";  //
	
	public static final String PAGE_SIZE = "10";//分页显示默认每页显示行数
	
	
	public final static class SYS_MENU {
		public final static long ROOT_MENU_PARENT_ID = -1l;
	}
	
	public final static class SYS_ORG {
		public final static long ROOT_ORG_PARENT_ID = -1l;
		public final static long COMPANY_TYPE = 0;
		public final static long DEPARTMENT_TYPE = 1;
	}
	
	public final static class SYS_USER {
		public final static String INIT_USER_PASSWORD = "123456";
		public final static int INIT_USER_STATUS = -1;//小于0为不可登录
		public final static int INIT_USER_LOGIN_STATUS = 1;//可登录
	}
	
	public static final String DEFUALT_MODEL_NAME ="CCF";
	public static final String CA_MAINTENANCE ="CA maintenance";
	public static final String CA_REPORT ="CA report";
	public static final String CA_MAINTENANCE_RPT ="CA maintenanceRPT";
	public static final String CA_MAINTENANCE_REPORT ="mapping CA";
	public static final String Saving_MAINTENANCE_RPT ="Saving maintenanceRPT";
	public static final String MARKUP_MAINTENANCE ="MarkupMaintenance";
	public static final String SUMMARY_ACTUAL_DETAIL ="SummaryActualDetail";
	public static final String SUMMARY_ACTUAL ="SummaryActual";
	public static final String VARIANCE_ANALYSIS_SUMMARY ="variance_analysis_summary";

	public static final String MY_DEMO ="My_demo";

	public static final String OTHER_CATEGORY ="OtherCategory";
	
	public static final String BMC_QTQ_SUMMARY_WW ="BmcQtqSummaryWW";
	public static final String BMC_QTQ_SUMMARY_PRC ="BmcQtqSummaryPRC";
	public static final String BMC_QTQ_SUMMARY_RPT ="BmcQtqSummaryRPT";
	
	public static final String BMC_QTQ_DETAIL_WW ="BmcQtqDetailWW";
	public static final String BMC_QTQ_DETAIL_PRC ="BmcQtqDetailPRC";

	public static final String ACTUAL_FCST_TABLE_NAME ="act_vs_fcst";
	
	public static final String ACTUAL_FCST__NEWEBR_TABLE_NAME ="act_vs_fcst_newebr";
	
	public static final String ACTUAL_FCST__PRC_TABLE_NAME ="act_vs_fcst_prc";
	
	public static final String ACTUAL_FCST ="ActualFcst";
	public static final String ACTUAL_FCST_NEWEBR ="ActualFcstNewebr";
	public static final String ACTUAL_FCST_PRC ="ActualFcstPrc";
	public static final String ACTUAL_FCST_FOR_SUMMARY ="ActualFcstForSummary";
	public static final String ACTUAL_FCST_NEWEBR_FOR_SUMMARY ="ActualFcstNewebrForSummary";

	
	public static final String ACTUAL_FCST_NAME ="actual_fcst";
	
	public static final String ACTUAL_FCST_NEWEBR_NAME ="actual_fcst_newebr";
	
	public static final String ACTUAL_FCST_PRC_NAME ="actual_fcst_prc";
	
	public static final String ACTUAL_FCST_PRC_FOR_SUMMARY ="actual_fcst_prc";
	
	public static final String RPT_COST_TAPE ="rpt_cost_tape";

	public static final String VARIANCE_ANGLYSIS_DETAIL ="variance_analysis_detail";

	public static final String PN_QTQ ="pn_qtq";
	
	public static final String OPTIMUS_COST_CREDIT ="OptimusCostCredit";
	
	public static final String EBR_DATA_VALIDATION ="ebr_data_validation";
	
	public static final String PN_QTQ_DETAIL_LOAD ="PnQtqDetail";
	
	public static final String PN_QTQ_SUMMARY_LOAD ="PnQtqSummary";
	
	public static final String RPT_COST_TAPE_LOAD ="CostTape";
	
	public static final String CPU_SHPT_REPORT ="CPUShptReport";
	
	public static int [] months = {1,2,3,4,5,6,7,8,9,10,11,12};
	
	public static String[] monthStr = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

	public static String MONTH_NAME = "month";
	
	public static int MONTH_LENGTH = 6;
	
	public static String SEGMENT_TYPE_WW = "ww";
	
	public static String SEGMENT_TYPE_PRC = "prc";
	
}
