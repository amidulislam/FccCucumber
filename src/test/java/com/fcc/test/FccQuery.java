package com.fcc.test;

import java.lang.reflect.Field;

public class FccQuery
{
	//--Query For Loan count(Credit, LE , Locked,Funded) encompass DB
	private String slap_Dashboard_Grid_LoanCount=" SELECT BranchOrgCode,\r\n" + 
			"               CASE\r\n" + 
			"                   WHEN CoManager IS NULL\r\n" + 
			"                        OR CoManager = ''\r\n" + 
			"                   THEN CASE\r\n" + 
			"                            WHEN EP.FirstName IS NULL\r\n" + 
			"                            THEN 'Branch'\r\n" + 
			"                            ELSE EP.FirstName + ' ' + Ep.LastName\r\n" + 
			"                        END\r\n" + 
			"                   ELSE CASE\r\n" + 
			"                            WHEN LOWER(Ep.LastName) < LOWER(Ec.LastName)\r\n" + 
			"                            THEN Ep.LastName + '/' + Ec.LastName\r\n" + 
			"                            ELSE Ec.LastName + '/' + Ep.LastName\r\n" + 
			"                        END\r\n" + 
			"               END AS BranchManagerName,\r\n" + 
			"               CASE\r\n" + 
			"                   WHEN bo.Active = 'Y'\r\n" + 
			"                   THEN 'Active'\r\n" + 
			"                   WHEN bo.Active = 'N'\r\n" + 
			"                   THEN 'InActive'\r\n" + 
			"               END AS IsActive\r\n" + 
			"        INTO #tbl_branches\r\n" + 
			"        FROM BranchOrganization BO WITH(NOLOCK)\r\n" + 
			"             LEFT JOIN Employee EP WITH(NOLOCK) ON EP.UserName = BO.PrimaryManager\r\n" + 
			"             LEFT JOIN Employee EC WITH(NOLOCK) ON EC.UserName = BO.CoManager\r\n" + 
			"        WHERE BranchOrgCode LIKE '___';\r\n" + 
			"\r\n" + 
			" \r\n" + 
			"\r\n" + 
			"        select count(*) \r\n" + 
			"        FROM [Supreme-Dbase1].emdb.emdbuser.LoanSummary L WITH(NOLOCK)\r\n" + 
			"                    INNER JOIN [Supreme-Dbase1].emdb.emdbuser.LOANXDB_D_01 D01 WITH(NOLOCK) ON D01.XrefId = L.XRefID\r\n" + 
			"					INNER JOIN [Supreme-Dbase1].emdb.emdbuser.LOANXDB_D_02 D02 WITH(NOLOCK) ON D02.XrefId = L.XRefID\r\n" + 
			"                    INNER JOIN [Supreme-Dbase1].emdb.emdbuser.LOANXDB_S_02 S02 WITH(NOLOCK) ON S02.XrefId = L.XRefID\r\n" + 
			"                    LEFT JOIN [supreme-dbase1].supremereports.dbo.employee emp WITH(NOLOCK) ON l.LoanOfficerId = emp.EncompassUserName\r\n" + 
			"                    LEFT JOIN [Supreme-Dbase5].sl_net_2011.dbo.vw_Web_InitialLock V WITH(NOLOCK) ON V.LoanNumber = L.LoanNumber\r\n" + 
			"                    LEFT JOIN #tbl_branches br WITH(NOLOCK) ON _orgid = br.BranchOrgCode\r\n" + 
			"               WHERE L.loanfolder NOT IN\r\n" + 
			"               (\r\n" + 
			"                   SELECT LoanFolder\r\n" + 
			"                   FROM [supreme-dbase1].[SupremeReports].[dbo].[LoanFolderExclusion] WITH(NOLOCK)\r\n" + 
			"                   WHERE LoanFolder <> 'Risk'\r\n" + 
			"               ) and #ColumnName  between '#StartDate' and '#EndDate' and br.BranchOrgCode in(#branchList)\r\n" + 
			"				 and  emp.TerminationDate is NULL\r\n" + 
			"				 and  L.LienPosition not like '%SECOND%'\r\n" + 
			"                 and L.LoanOfficerName like '%#LoName%'\r\n" + 
			"drop table #tbl_branches";
	//========================================================================================================================================================================================================================================
	//--Query For Loan count(Credit, LE , Locked,Funded) encompass DB
	//private String slap_Dashboard_Grid_LoanCount_FccDB="select count(*) from ATI_BEM_DW_DIM_Slap with(NoLock) where #ColumnName between '#StartDate' and '#EndDate' and LienPosition Not like '%SECOND%' and branch in(#branchList) and TerminationDate is null and LoanFolder<>'Risk' and LoanOfficerName like '%#LoName%'";
	//Column 1-4
	private String Credit_FccDB="select count(*) from ATI_BEM_DW_DIM_Slap with(NoLock) where #ColumnName between '#StartDate' and '#EndDate' and LienPosition Not like '%SECOND%' and branch in(#branchList) and TerminationDate is null and LoanFolder<>'Risk' and LoanOfficerName like '%#LoName%'";
	private String App_FccDB="select count(*) from ATI_BEM_DW_DIM_Slap with(NoLock) where #ColumnName between '#StartDate' and '#EndDate' and LienPosition Not like '%SECOND%' and branch in(#branchList) and TerminationDate is null and LoanFolder<>'Risk' and LoanOfficerName like '%#LoName%'";
	
	private String Locks_FccDB="select count(*) from ATI_BEM_DW_DIM_Slap with(NoLock) where #ColumnName between '#StartDate' and '#EndDate' and LienPosition Not like '%SECOND%' and branch in(#branchList) and TerminationDate is null and LoanFolder<>'Risk' and LoanOfficerName like '%#LoName%'";
	private String Locks_FccDB_BranchLevel="select count(*) from ATI_BEM_DW_DIM_Slap with(NoLock) where LockDate between '#StartDate' and '#EndDate' and LienPosition Not like '%SECOND%' and branch in(#branchList) and TerminationDate is null and LoanFolder<>'Risk'";
	private String Locks_FccDB_LoLevel="select count(*) from ATI_BEM_DW_DIM_Slap with(NoLock) where LockDate between '#StartDate' and '#EndDate' and LienPosition Not like '%SECOND%' and branch in(#branchList) and TerminationDate is null and LoanFolder<>'Risk' and LoanOfficerName like '%#LoName%'";
	
	private String Funded_FccDB="select count(*) from ATI_BEM_DW_DIM_Slap with(NoLock) where #ColumnName between '#StartDate' and '#EndDate' and LienPosition Not like '%SECOND%' and branch in(#branchList) and TerminationDate is null and LoanFolder<>'Risk' and LoanOfficerName like '%#LoName%'";
	private String Funded_FccDB_BranchLevel="select count(*) from ATI_BEM_DW_DIM_Slap with(NoLock) where FundedDate between '#StartDate' and '#EndDate' and LienPosition Not like '%SECOND%' and branch in(#branchList) and TerminationDate is null and LoanFolder<>'Risk'";
	private String Funded_FccDB_LoLevel="select count(*) from ATI_BEM_DW_DIM_Slap with(NoLock) where FundedDate between '#StartDate' and '#EndDate' and LienPosition Not like '%SECOND%' and branch in(#branchList) and TerminationDate is null and LoanFolder<>'Risk' and LoanOfficerName like '%#LoName%'";
	//========================================================================================================================================================================================================================================	
	//Query For Lock Vol(MTD Activity) Slap Grids;
	//Column 5
	private String Lock_Vol_FccDB="select sum(case when month('#StartDate') = month(getdate()) then           \r\n" + 
			"    case when Log_MS_DATE_Funding is null and  LockExpirationDate >getdate() and log_MS_Status_Funding <>'Achieved' and LoanFolder in ('My Pipeline', 'Underwriting')            \r\n" + 
			"    then isnull(TotalLoanAmount,0) else 0 end           \r\n" + 
			"    when #ColumnName between '#StartDate' and '#EndDate' then isnull(TotalLoanAmount,0)           \r\n" + 
			"    else 0 end) as LocksVol  from ATI_BEM_DW_DIM_Slap with(NoLock) where 	\r\n" + 
			"	branch in(#branchList)\r\n" + 
			"and LoanOfficerName like '%#LoName%'\r\n" + 
			"--and LienPosition Not like '%SECOND%' \r\n" + 
			"and TerminationDate is null and LoanFolder<>'Risk' ";
	//========================================================================================================================================================================================================================================
	//--Query For Lock Target BPS
	//Column 6
	private String Lock_Target_BPS_FccDB="\r\n" + 
			" Declare @startDate Date='#StartDate'\r\n" + 
			"Declare @EndDate Date='#EndDate' \r\n" +
			"declare @LoanOfficerName varchar(max)='%#LoName%'                           \r\n" +
			"declare @columnName varchar(max)='#ColumnName' \r\n"+
			"declare @Branch varchar(max) = #branchList                               \r\n" + 
			"         \r\n" + 
			"declare @TotalLockAmount decimal (20,2), @TotalFundedAmount decimal (20,2)\r\n" + 
			"          \r\n" + 
			" select           \r\n" + 
			"  @TotalLockAmount =sum(case when LockDate between @StartDate and @EndDate then isnull(TotalLoanAmount,0) else 0 end)          \r\n" + 
			" ,@TotalFundedAmount = sum(case when FundedDate between @StartDate and @EndDate then isnull(TotalLoanAmount,0) else 0 end)          \r\n" + 
			" from ATI_BEM_DW_DIM_Slap           \r\n" + 
			" where (LockDate between @StartDate and @EndDate          \r\n" + 
			" OR FundedDate between @StartDate and @EndDate) \r\n" + 
			"\r\n" + 
			" \r\n" + 
			"\r\n" + 
			"  print @TotalLockAmount\r\n" + 
			"\r\n" + 
			" \r\n" + 
			"\r\n" + 
			" select           \r\n" + 
			" sum(case when month(@startDate) = month(getdate()) then           \r\n" + 
			"    case when Log_MS_DATE_Funding is null and  LockExpirationDate >getdate() and log_MS_Status_Funding <>'Achieved' and LoanFolder in ('My Pipeline', 'Underwriting')            \r\n" + 
			"    then isnull(TotalLoanAmount,0) else 0 end           \r\n" + 
			"    when LockDate between @StartDate and @EndDate then isnull(TotalLoanAmount,0)           \r\n" + 
			"    else 0 end) as LocksVol,\r\n" + 
			"\r\n" + 
			"	sum(case when month(@startDate) = month(getdate()) then           \r\n" + 
			"    case when Log_MS_DATE_Funding is null and  LockExpirationDate >getdate() and log_MS_Status_Funding <>'Achieved' and LoanFolder in ('My Pipeline', 'Underwriting')            \r\n" + 
			"    then isnull(ActualRevenue,0) else 0 end           \r\n" + 
			"    when LockDate between @StartDate and @EndDate then isnull(ActualRevenue,0)           \r\n" + 
			"    else 0 end) as ActualRevLock,\r\n" + 
			"\r\n" + 
			"sum( case when LockDate between @StartDate and @EndDate then isnull(TargetRevenue,0) else 0 end) as LocksTargetVol,\r\n" + 
			"sum( case when month(@startDate) = month(getdate()) then           \r\n" + 
			"    case when Log_MS_DATE_Funding is null and  LockExpirationDate >getdate() and log_MS_Status_Funding <>'Achieved' and LoanFolder in ('My Pipeline', 'Underwriting')            \r\n" + 
			"    then isnull(TargetRevenue,0) else 0 end           \r\n" + 
			"    when LockDate between @StartDate and @EndDate then isnull(TargetRevenue,0)           \r\n" + 
			"    else 0 end) as TargetRevLock  \r\n" + 
			"into #TempLockBps\r\n" + 
			"from ATI_BEM_DW_DIM_Slap where branch in(select data from dbo.Split(@Branch, ','))\r\n" + 
			"and LoanOfficerName like @LoanOfficerName\r\n" + 
			"--and LoanOfficerName <> ''\r\n" + 
			"--and LienPosition Not like '%SECOND%' \r\n" + 
			"and TerminationDate is null \r\n" + 
			"\r\n" + 
			"select \r\n" + 
			" case when sl.LocksVol <> 0 then round(((sl.TargetRevLock / sl.LocksVol) * 10000 ),0) else 0 end as lockTargetBPS \r\n"+
						 
			" from #TempLockBps sl\r\n" +
			 
			"\r\n" + 
			" drop table #TempLockBps\r\n";
	//========================================================================================================================================================================================================================================
	//--Query For Lock BPS
	//Column 7
	private String Lock_BPS_FccDB="\r\n" + 
			" Declare @startDate Date='#StartDate'\r\n" + 
			"Declare @EndDate Date='#EndDate' " +
			"declare @LoanOfficerName varchar(max)='%#LoName%'                           \r\n" +
			"declare @columnName varchar(max)='#ColumnName'"+
			"declare @Branch varchar(max) = #branchList                               \r\n" + 
			"         \r\n" + 
			"declare @TotalLockAmount decimal (20,2), @TotalFundedAmount decimal (20,2)\r\n" + 
			"          \r\n" + 
			" select           \r\n" + 
			"  @TotalLockAmount =sum(case when LockDate between @StartDate and @EndDate then isnull(TotalLoanAmount,0) else 0 end)          \r\n" + 
			" ,@TotalFundedAmount = sum(case when FundedDate between @StartDate and @EndDate then isnull(TotalLoanAmount,0) else 0 end)          \r\n" + 
			" from ATI_BEM_DW_DIM_Slap           \r\n" + 
			" where (LockDate between @StartDate and @EndDate          \r\n" + 
			" OR FundedDate between @StartDate and @EndDate) \r\n" + 
			"\r\n" + 
			" \r\n" + 
			"\r\n" + 
			"  print @TotalLockAmount\r\n" + 
			"\r\n" + 
			" \r\n" + 
			"\r\n" + 
			" select           \r\n" + 
			" sum(case when month(@startDate) = month(getdate()) then           \r\n" + 
			"    case when Log_MS_DATE_Funding is null and  LockExpirationDate >getdate() and log_MS_Status_Funding <>'Achieved' and LoanFolder in ('My Pipeline', 'Underwriting')            \r\n" + 
			"    then isnull(TotalLoanAmount,0) else 0 end           \r\n" + 
			"    when LockDate between @StartDate and @EndDate then isnull(TotalLoanAmount,0)           \r\n" + 
			"    else 0 end) as LocksVol,\r\n" + 
			"\r\n" + 
			"	sum(case when month(@startDate) = month(getdate()) then           \r\n" + 
			"    case when Log_MS_DATE_Funding is null and  LockExpirationDate >getdate() and log_MS_Status_Funding <>'Achieved' and LoanFolder in ('My Pipeline', 'Underwriting')            \r\n" + 
			"    then isnull(ActualRevenue,0) else 0 end           \r\n" + 
			"    when LockDate between @StartDate and @EndDate then isnull(ActualRevenue,0)           \r\n" + 
			"    else 0 end) as ActualRevLock,\r\n" + 
			"\r\n" + 
			"sum( case when LockDate between @StartDate and @EndDate then isnull(TargetRevenue,0) else 0 end) as LocksTargetVol,\r\n" + 
			"sum( case when month(@startDate) = month(getdate()) then           \r\n" + 
			"    case when Log_MS_DATE_Funding is null and  LockExpirationDate >getdate() and log_MS_Status_Funding <>'Achieved' and LoanFolder in ('My Pipeline', 'Underwriting')            \r\n" + 
			"    then isnull(TargetRevenue,0) else 0 end           \r\n" + 
			"    when LockDate between @StartDate and @EndDate then isnull(TargetRevenue,0)           \r\n" + 
			"    else 0 end) as TargetRevLock  \r\n" + 
			"into #TempLockBps\r\n" + 
			"from ATI_BEM_DW_DIM_Slap where branch in(select data from dbo.Split(@Branch, ','))\r\n" + 
			"and LoanOfficerName like @LoanOfficerName\r\n" + 
			"--and LoanOfficerName <> ''\r\n" + 
			"--and LienPosition Not like '%SECOND%' \r\n" + 
			"and TerminationDate is null \r\n" + 
			"\r\n" + 
			"select \r\n" + 
			" case when sl.LocksVol <> 0 then round(((sl.ActualRevLock / sl.LocksVol) * 10000 ),0) else 0 end as lockBPS\r\n" +						 
			" from #TempLockBps sl\r\n" + 
			"\r\n" +			
			"\r\n" + 
			" drop table #TempLockBps\r\n";
			
//========================================================================================================================================================================================================================================
	//--Query For Funded volume
	//Column 8
private String Funded_Vol_FccDB="Declare @startDate Date='#StartDate'\r\n" + 
		"			Declare @EndDate Date='#EndDate'\r\n" + 
		"			declare @LoanOfficerName varchar(max)='%#LoName%'      			\r\n" + 
		"			declare @Branch varchar(max) = #branchList    			\r\n" + 
		"			declare @columnName varchar(max)='#ColumnName'			\r\n"+
		"select\r\n" + 
		"sum( case when FundedDate between @StartDate and @EndDate then isnull(TotalLoanAmount,0) else 0 end) as FundedVol\r\n" + 
		"from ATI_BEM_DW_DIM_Slap \r\n" + 
		"where branch in(select data from dbo.Split(@Branch, ','))\r\n" + 
		"and LoanOfficerName like @LoanOfficerName\r\n" + 
		"--and LoanOfficerName <> ''\r\n" + 
		"--and LienPosition Not like '%SECOND%'\r\n" + 
		"and TerminationDate is null";
//========================================================================================================================================================================================================================================
//--Query For Funded Target BPS
//Column 9
private String Funded_Target_BPS_FccDB="Declare @startDate Date='#StartDate'\r\n" + 
		"					Declare @EndDate Date='#EndDate'\r\n" + 
		"					Declare @LoanOfficerName varchar(max)='%#LoName%'      			\r\n" + 
		"					Declare @Branch varchar(max) = #branchList	\r\n" + 
		"					Declare @TotalFundedAmount decimal (20,2)\r\n" + 
		"				    Declare @columnName varchar(max)='#ColumnName'	\r\n" + 
		"select            \r\n" + 
		" @TotalFundedAmount = sum(case when FundedDate between @StartDate and @EndDate then isnull(TotalLoanAmount,0) else 0 end)          \r\n" + 
		" from ATI_BEM_DW_DIM_Slap           \r\n" + 
		" where (LockDate between @StartDate and @EndDate          \r\n" + 
		" OR FundedDate between @StartDate and @EndDate)\r\n" + 
		" and branch in(select data from dbo.Split(@Branch, ','))\r\n" + 
		"		and LoanOfficerName like @LoanOfficerName		\r\n" + 
		"		and TerminationDate is null\r\n" + 
		"print  @TotalFundedAmount\r\n" + 
		"select \r\n" + 
		"sum( case when FundedDate between @StartDate and @EndDate then isnull(TargetRevenue,0) else 0 end) as FundedTargetVol,\r\n" + 
		"sum( case when FundedDate between @StartDate and @EndDate then isnull(TotalLoanAmount,0) else 0 end) as FundedVol\r\n" + 
		"into #SlapMasterData          \r\n" + 
		"from ATI_BEM_DW_DIM_Slap SL\r\n" + 
		"where branch in(select data from dbo.Split(@Branch, ','))\r\n" + 
		"		and LoanOfficerName like @LoanOfficerName		\r\n" + 
		"		and TerminationDate is null\r\n" + 
		"\r\n" + 
		"select \r\n" + 
		"case when sl.FundedVol <> 0 then round(((sl.FundedTargetVol / @TotalFundedAmount) * 10000 ),0) else 0 end as FundedTargetBPS\r\n" + 
		"from  #SlapMasterData sl\r\n" + 
		"drop table #SlapMasterData";


//========================================================================================================================================================================================================================================
//--Query For Funded BPS   Under Pipeline
//Column 10
private String Funded_BPS_FccDB="Declare @startDate Date='#StartDate'\r\n" + 
		"			Declare @EndDate Date='#EndDate'\r\n" + 
		"			declare @LoanOfficerName varchar(max)='%#LoName%'      			\r\n" + 
		"			declare @Branch varchar(max) = #branchList 			\r\n" + 
		"			Declare @columnName varchar(max)='#ColumnName'	\r\n" + 
		"select\r\n" + 
		"sum( case when FundedDate between @StartDate and @EndDate then isnull(BlastProcessing,0) else 0 end) as BlastProcessing,          \r\n" + 
		"sum( case when FundedDate between @StartDate and @EndDate then isnull(BlastRevenue,0) else 0 end) as BlastRevenue,          \r\n" + 
		"sum( case when FundedDate between @StartDate and @EndDate then isnull(BlastAmount,0) else 0 end) as BlastAmount\r\n" + 
		"into #Temp\r\n" + 
		"from ATI_BEM_DW_DIM_Slap \r\n" + 
		"where branch in(select data from dbo.Split(@Branch, ','))\r\n" + 
		"and LoanOfficerName like @LoanOfficerName\r\n" + 
		"and TerminationDate is null\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"select\r\n" + 
		"case when round(BlastRevenue,0)<>0 \r\n" + 
		"then round(((BlastRevenue/BlastAmount*10000)-(BlastProcessing/BlastAmount*10000)),0)\r\n" + 
		"else 0\r\n" + 
		"end as FundedBPS\r\n" + 
		"from #Temp\r\n" + 
		"drop table #Temp\r\n"; 
//========================================================================================================================================================================================================================================
//--Query For Locked Under Pipeline
//Column 11
//--Locked(Pipeline) query
private String Locked_FccDB="Declare @startDate Date='#StartDate'\r\n" + 
		"Declare @EndDate Date='#EndDate'\r\n" + 
		"Declare @LoanOfficerName varchar(max)='%#LoName%' \r\n" + 
		"Declare @Branch varchar(max) = #branchList\r\n" + 
		"Declare @TotalFundedAmount decimal (20,2)\r\n" + 
		"Declare @columnName varchar(max)='#ColumnName'		\r\n" +		
		"declare @LienPosition varchar(max)='%SECOND%'\r\n" + 
		"\r\n" + 
		"select            \r\n" + 
		"@TotalFundedAmount = sum(case when FundedDate between @StartDate and @EndDate then isnull(TotalLoanAmount,0) else 0 end)          \r\n" + 
		"from ATI_BEM_DW_DIM_Slap           \r\n" + 
		"where (LockDate between @StartDate and @EndDate          \r\n" + 
		"OR FundedDate between @StartDate and @EndDate)\r\n" + 
		"and branch in(select data from dbo.Split(@Branch, ','))\r\n" + 
		"and LoanOfficerName like @LoanOfficerName		\r\n" + 
		"and TerminationDate is null\r\n" + 
		"\r\n" + 
		"print  @TotalFundedAmount\r\n" + 
		"\r\n" + 
		"select\r\n" + 
		"sum( case when _1393 ='Active Loan' and LienPosition Not like @LienPosition then 1 else 0 end) as PipelineLocked\r\n" + 
		"from ATI_BEM_DW_DIM_Slap \r\n" + 
		"where branch in(select data from dbo.Split(@Branch, ','))\r\n" + 
		"and LoanOfficerName like @LoanOfficerName\r\n" + 
		"--and LoanOfficerName <> ''\r\n" + 
		"--and LienPosition Not like '%SECOND%'\r\n" + 
		"and TerminationDate is null";
//========================================================================================================================================================================================================================================
//--Query For Prior to UW
//Column 12
private String  Prior_to_UW_FccDB="Declare @startDate Date='#StartDate'\r\n" + 
		"Declare @EndDate Date='#EndDate'\r\n" + 
		"Declare @LoanOfficerName varchar(max)='%#LoName%'      			\r\n" + 
		"Declare @Branch varchar(max) = #branchList\r\n" + 
		"Declare @TotalFundedAmount decimal (20,2)\r\n" + 
		"Declare @LienPosition varchar(50) = '%SECOND%'\r\n" + 
		"Declare @columnName varchar(max)='#ColumnName'\r\n" + 
		"\r\n" + 
		"select\r\n" + 
		"sum( case when ((_MS_STATUS in('Sent to processing','Sent to Coord')OR (_MS_STATUS= 'Started' and ApplicationDate is not null)       \r\n" + 
		"or (_MS_STATUS= 'Started' and ApplicationDate is null and LockDate is not null))  and LienPosition Not like @LienPosition) then 1 else 0 end) as PriortoUW\r\n" + 
		"from ATI_BEM_DW_DIM_Slap\r\n" + 
		" where branch in(select data from dbo.Split(@Branch, ','))\r\n" + 
		"		and LoanOfficerName like @LoanOfficerName		\r\n" + 
		"		and TerminationDate is null";
//========================================================================================================================================================================================================================================
//--Query For In UW
//Column 13
private String In_UW_FccDB="Declare @startDate Date='#StartDate'\r\n" + 
		"					Declare @EndDate Date='#EndDate'\r\n" + 
		"					Declare @LoanOfficerName varchar(max)='%#LoName%'      			\r\n" + 
		"					Declare @Branch varchar(max) = #branchList\r\n" + 
		"					Declare @TotalFundedAmount decimal (20,2)\r\n" + 
		"					Declare @LienPosition varchar(50) = '%SECOND%'\r\n" +
		"					Declare @columnName varchar(max)='#ColumnName'\r\n"+	
		"select\r\n" + 
		"sum( case when _MS_STATUS in('Submitted','Underwriting Received','Decision','Approved')  and LienPosition Not like @LienPosition then 1 else 0 end) as InUW\r\n" + 
		"from ATI_BEM_DW_DIM_Slap\r\n" + 
		" where branch in(select data from dbo.Split(@Branch, ','))\r\n" + 
		"		and LoanOfficerName like @LoanOfficerName		\r\n" + 
		"		and TerminationDate is null";
//========================================================================================================================================================================================================================================
//--Query For CTC Docs
//Column 14
private String CTC_Docs_FccDB="Declare @startDate Date='#StartDate'\r\n" + 
		"			Declare @EndDate Date='#EndDate'\r\n" + 
		"			Declare @LoanOfficerName varchar(max)='%#LoName%'      			\r\n" + 
		"			Declare @Branch varchar(max) = #branchList\r\n" + 
		"			declare @TotalFundedAmount decimal (20,2)\r\n" + 
		"			declare @LienPosition varchar(50) = '%SECOND%';\r\n" + 
		"			Declare @columnName varchar(max)='#ColumnName'\r\n"+
		"\r\n" + 
		"select count(*) as TotalLoans, slap.Branch, LoanOfficerName      \r\n" + 
		"into #FundedLoanCount      \r\n" + 
		"from  ATI_BEM_DW_DIM_Slap slap      \r\n" + 
		"left join BranchOrganization BO on BO.BranchOrgName = slap.Branch      \r\n" + 
		"where slap.Branch in (select data from dbo.Split(@Branch, ',') )        \r\n" + 
		"and ApplicationDate between @StartDate and @EndDate      \r\n" + 
		"and FundedDate between @StartDate and @EndDate    \r\n" + 
		"and TerminationDate is null\r\n" + 
		" and LienPosition Not like @LienPosition\r\n" + 
		" and slap.LoanOfficerName like @LoanOfficerName		\r\n" + 
		"group by slap.Branch, LoanOfficerName \r\n" + 
		"\r\n" + 
		"select\r\n" + 
		"sum( case when _MS_STATUS in('Clear To Close','Docs Requested','Docs Out','Funds Ordered','Funds Sent','Doc signed') and LienPosition Not like @LienPosition then 1 else 0 end) as CTCDocs\r\n" + 
		"\r\n" + 
		"from ATI_BEM_DW_DIM_Slap SL\r\n" + 
		"left join #FundedLoanCount FL      \r\n" + 
		"on SL.Branch = FL.Branch and         \r\n" + 
		"SL.LoanOfficerName = FL.LoanOfficerName  \r\n" + 
		" where SL.Branch in(select data from dbo.Split(@Branch, ','))\r\n" + 
		"		and SL.LoanOfficerName like @LoanOfficerName		\r\n" + 
		"		and TerminationDate is null\r\n" + 
		"drop table #FundedLoanCount\r\n";
		
//========================================================================================================================================================================================================================================
//--Query For In All IN
//Column 15
private String All_In_FccDB="Declare @startDate Date='#StartDate'\r\n" + 
		"					Declare @EndDate Date='#EndDate'\r\n" + 
		"					Declare @LoanOfficerName varchar(max)='%#LoName%'      			\r\n" + 
		"					Declare @Branch varchar(max) = #branchList\r\n" + 
		"					Declare @TotalFundedAmount decimal (20,2)\r\n" + 
		"					Declare @LienPosition varchar(50) = '%SECOND%'\r\n" + 
		"					Declare @columnName varchar(max)='#ColumnName'\r\n"+
		"\r\n" + 
		"select count(*) as TotalLoans, slap.Branch, LoanOfficerName      \r\n" + 
		"into #FundedLoanCount      \r\n" + 
		"from  ATI_BEM_DW_DIM_Slap slap      \r\n" + 
		"left join BranchOrganization BO on BO.BranchOrgName = slap.Branch      \r\n" + 
		"where slap.Branch in (select data from dbo.Split(@Branch, ',') )        \r\n" + 
		"and ApplicationDate between @StartDate and @EndDate      \r\n" + 
		"and FundedDate between @StartDate and @EndDate    \r\n" + 
		"and TerminationDate is null\r\n" + 
		" and LienPosition Not like @LienPosition\r\n" + 
		"group by slap.Branch, LoanOfficerName \r\n" + 
		"\r\n" + 
		"select sum( case when           \r\n" + 
		"((_MS_STATUS in('Clear To Close','Docs Requested','Docs Out','Funds Ordered','Funds Sent','Doc signed') or        \r\n" + 
		"Log_MS_Date_Funding between @StartDate and @EndDate ) and LienPosition Not like @LienPosition)      \r\n" + 
		"then 1 else 0 end) as AllIn\r\n" + 
		"\r\n" + 
		"from ATI_BEM_DW_DIM_Slap SL\r\n" + 
		"left join #FundedLoanCount FL      \r\n" + 
		"on SL.Branch = FL.Branch and         \r\n" + 
		"SL.LoanOfficerName = FL.LoanOfficerName  \r\n" + 
		" where SL.Branch in(select data from dbo.Split(@Branch, ','))\r\n" + 
		"		and SL.LoanOfficerName like @LoanOfficerName		\r\n" + 
		"		and TerminationDate is null\r\n" + 
		"\r\n" + 
		"drop table #FundedLoanCount\r\n";
//========================================================================================================================================================================================================================================
//--Query For Cycle Time
//Column 16
private String Cycle_Time_FccDB="\r\n" + 
		"					Declare @startDate Date='#StartDate'\r\n" + 
		"					Declare @EndDate Date='#EndDate'\r\n" + 
		"					Declare @LoanOfficerName varchar(max)='%#LoName%'      			\r\n" + 
		"					Declare @Branch varchar(max) = #branchList\r\n" + 
		"					Declare @TotalFundedAmount decimal (20,2)\r\n" + 
		"					Declare @LienPosition varchar(50) = '%SECOND%'\r\n" +
		"					Declare @columnName varchar(max)='#ColumnName'\r\n"+
		"\r\n" + 
		"\r\n" + 
		"select count(*) as TotalLoans, slap.Branch, LoanOfficerName      \r\n" + 
		"into #FundedLoanCount      \r\n" + 
		"from  ATI_BEM_DW_DIM_Slap slap      \r\n" + 
		"left join BranchOrganization BO on BO.BranchOrgName = slap.Branch      \r\n" + 
		"where slap.Branch in (select data from dbo.Split(@Branch, ',') )        \r\n" + 
		"and ApplicationDate between @StartDate and @EndDate      \r\n" + 
		"and FundedDate between @StartDate and @EndDate    \r\n" + 
		"and TerminationDate is null\r\n" + 
		" and LienPosition Not like @LienPosition\r\n" + 
		"group by slap.Branch, LoanOfficerName \r\n" + 
		"\r\n" + 
		"select           \r\n" + 
		"Round(SUM(case when FundedDate is not null and FL.TotalLoans >0 and FundedDate between @StartDate and @EndDate AND ApplicationDate between @StartDate and @EndDate then Cast(datediff(day,ApplicationDate,FundedDate) as decimal(9,2))/FL.TotalLoans else 0 end),0) as    \r\n" + 
		"CycleTime  \r\n" + 
		"from ATI_BEM_DW_DIM_Slap SL\r\n" + 
		"left join #FundedLoanCount FL      \r\n" + 
		"on SL.Branch = FL.Branch and         \r\n" + 
		"SL.LoanOfficerName = FL.LoanOfficerName  \r\n" + 
		" where SL.Branch in(select data from dbo.Split(@Branch, ','))\r\n" + 
		"		and SL.LoanOfficerName like @LoanOfficerName		\r\n" + 
		"		and TerminationDate is null\r\n" + 
		"drop table #FundedLoanCount\r\n";
//========================================================================================================================================================================================================================================
//--Query For Revenue Lock
//Column 17

String Lock_FccDB="Declare @startDate Date='#StartDate'\r\n" + 
		"Declare @EndDate Date='#EndDate'\r\n" + 
		"declare @LoanOfficerName varchar(max)='%#LoName%'      			\r\n" + 
		"declare @Branch varchar(max) = #branchList \r\n" + 
		"declare @TotalFundedAmount decimal (20,2)\r\n" + 
		"declare @LienPosition varchar(50) = '%SECOND%'\r\n" +
		"Declare @columnName varchar(max)='#ColumnName'\r\n"+
		"\r\n" + 
		"select \r\n" + 
		"sum(case when LockDate is not null and LockDate between @StartDate and @EndDate then ActualRevenue else 0 end) as RevenueLock,          -- $Amount\r\n" + 
		"sum(case when FundedDate is not null and FundedDate between @StartDate and @EndDate then ActualRevenue else 0 end) as RevenueFunded,           -- $Amount\r\n" + 
		"sum(case when (FundedDate is not null and FundedDate between @StartDate and @EndDate) or (LockDate is not null and LockDate between @StartDate and @EndDate) then ActualRevenue else 0 end) as RevenueBlend,\r\n" + 
		"sum(case when LockDate is not null and LockDate between @StartDate and @EndDate then TotalLoanAmount else 0 end)as RevenueLockTotalLoanAmount,\r\n" + 
		"sum(case when FundedDate is not null and FundedDate between @StartDate and @EndDate then TotalLoanAmount else 0 end)as RevenueFundedTotalLoanAmount,\r\n" + 
		"sum(case when (FundedDate is not null and FundedDate between @StartDate and @EndDate) or (LockDate is not null and LockDate between @StartDate and @EndDate) then TotalLoanAmount else 0 end) as RevenueBlendTotalLoanAmount\r\n" + 
		"\r\n" + 
		"into #Temp\r\n" + 
		"from  ATI_BEM_DW_DIM_Slap SL\r\n" + 
		"where SL.Branch in(select data from dbo.Split(@Branch, ','))\r\n" + 
		"and SL.LoanOfficerName like @LoanOfficerName		\r\n" + 
		"and TerminationDate is null\r\n" + 
		"--and LienPosition not like @LienPosition\r\n" +        //Temporary comment
		"select\r\n" + 
		"\r\n" + 
		"case when lo.RevenueLock<> 0 then  round((lo.RevenueLock/lo.RevenueLockTotalLoanAmount * 10000),0) else 0 end as RevenueLock       --BPS   \r\n" + 
		"\r\n" + 
		"from #Temp lo\r\n" + 
		"\r\n" + 
		"drop table #Temp\r\n";
		
//========================================================================================================================================================================================================================================
//--Query For Revenue Funded
//Column 18
private String Revenue_Funded_FccDB="Declare @startDate Date='#StartDate'\r\n" + 
		"Declare @EndDate Date='#EndDate'\r\n" + 
		"declare @LoanOfficerName varchar(max)='%#LoName%'      			\r\n" + 
		"declare @Branch varchar(max) = #branchList \r\n" + 
		"declare @TotalFundedAmount decimal (20,2)\r\n" + 
		"declare @LienPosition varchar(50) = '%SECOND%';\r\n" + 
		"Declare @columnName varchar(max)='#ColumnName'\r\n"+
		"\r\n" + 
		"select \r\n" + 
		"sum(case when LockDate is not null and LockDate between @StartDate and @EndDate then ActualRevenue else 0 end) as RevenueLock,          -- $Amount\r\n" + 
		"sum(case when FundedDate is not null and FundedDate between @StartDate and @EndDate then ActualRevenue else 0 end) as RevenueFunded,           -- $Amount\r\n" + 
		"sum(case when (FundedDate is not null and FundedDate between @StartDate and @EndDate) or (LockDate is not null and LockDate between @StartDate and @EndDate) then ActualRevenue else 0 end) as RevenueBlend,\r\n" + 
		"sum(case when LockDate is not null and LockDate between @StartDate and @EndDate then TotalLoanAmount else 0 end)as RevenueLockTotalLoanAmount,\r\n" + 
		"sum(case when FundedDate is not null and FundedDate between @StartDate and @EndDate then TotalLoanAmount else 0 end)as RevenueFundedTotalLoanAmount,\r\n" + 
		"sum(case when (FundedDate is not null and FundedDate between @StartDate and @EndDate) or (LockDate is not null and LockDate between @StartDate and @EndDate) then TotalLoanAmount else 0 end) as RevenueBlendTotalLoanAmount\r\n" + 
		"\r\n" + 
		"into #Temp\r\n" + 
		"from  ATI_BEM_DW_DIM_Slap SL\r\n" + 
		"where SL.Branch in(select data from dbo.Split(@Branch, ','))\r\n" + 
		"and SL.LoanOfficerName like @LoanOfficerName		\r\n" + 
		"and TerminationDate is null\r\n" + 
		"--and LienPosition not like @LienPosition\r\n" +      //Temporary Comment
		"select\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"case when lo.RevenueFunded<> 0 then  round((lo.RevenueFunded/lo.RevenueFundedTotalLoanAmount * 10000),0) else 0 end as RevenueFunded       --BPS   \r\n" + 
		"\r\n" + 
		"\r\n" + 
		"from #Temp lo\r\n" + 
		"\r\n" + 
		"drop table #Temp\r\n";
//========================================================================================================================================================================================================================================
//--Query For Revenue Blend
//Column 19
private String Revenue_Blend_FccDB="Declare @startDate Date='#StartDate'\r\n" + 
		"Declare @EndDate Date='#EndDate'\r\n" + 
		"declare @LoanOfficerName varchar(max)='%#LoName%'      			\r\n" + 
		"declare @Branch varchar(max) = #branchList \r\n" + 
		"declare @TotalFundedAmount decimal (20,2)\r\n" + 
		"declare @LienPosition varchar(50) = '%SECOND%';\r\n" +
		"Declare @columnName varchar(max)='#ColumnName'\r\n"+
		"\r\n" + 
		"select \r\n" + 
		"sum(case when LockDate is not null and LockDate between @StartDate and @EndDate then ActualRevenue else 0 end) as RevenueLock,          -- $Amount\r\n" + 
		"sum(case when FundedDate is not null and FundedDate between @StartDate and @EndDate then ActualRevenue else 0 end) as RevenueFunded,           -- $Amount\r\n" + 
		"sum(case when (FundedDate is not null and FundedDate between @StartDate and @EndDate) or (LockDate is not null and LockDate between @StartDate and @EndDate) then ActualRevenue else 0 end) as RevenueBlend,\r\n" + 
		"sum(case when LockDate is not null and LockDate between @StartDate and @EndDate then TotalLoanAmount else 0 end)as RevenueLockTotalLoanAmount,\r\n" + 
		"sum(case when FundedDate is not null and FundedDate between @StartDate and @EndDate then TotalLoanAmount else 0 end)as RevenueFundedTotalLoanAmount,\r\n" + 
		"sum(case when (FundedDate is not null and FundedDate between @StartDate and @EndDate) or (LockDate is not null and LockDate between @StartDate and @EndDate) then TotalLoanAmount else 0 end) as RevenueBlendTotalLoanAmount\r\n" + 
		"\r\n" + 
		"into #Temp\r\n" + 
		"from  ATI_BEM_DW_DIM_Slap SL\r\n" + 
		"where SL.Branch in(select data from dbo.Split(@Branch, ','))\r\n" + 
		"and SL.LoanOfficerName like @LoanOfficerName		\r\n" + 
		"and TerminationDate is null\r\n" + 
		"--and LienPosition not like @LienPosition\r\n" + //Modification is done here needs to remove,if required
		"select\r\n" + 
		"\r\n" + 
		"case when lo.RevenueBlend<>0 then  round((lo.RevenueBlend/lo.RevenueBlendTotalLoanAmount * 10000),0) else 0 end as RevenueBlend   --BPS\r\n" + 
		"\r\n" + 
		"from #Temp lo\r\n" + 
		"\r\n" + 
		"drop table #Temp\r\n";
//========================================================================================================================================================================================================================================
private String fundedTotal="  declare @start date = '#StartDate'            \r\n" + 
		" declare @end date = '#EndDate'        \r\n" + 
		" declare @branch varchar(2550) = #BranchList      \r\n" + 
		" declare @regions varchar(2550) = ''        \r\n" + 
		" declare @yearBy varchar(50)=''            \r\n" + 
		" declare @LoanDetail bit = 0               \r\n" + 
		" declare @LoanNumber varchar(255) = ''         \r\n" + 
		" declare @LoanOfficer varchar(255) =''         \r\n" + 
		" declare @user varchar(255) = 'ramakrishnan.s'   \r\n" + 
		"\r\n" + 
		" DECLARE @regionalBR TABLE (          \r\n" + 
		"  Branch VARCHAR(30),          \r\n" + 
		"  Region VARCHAR(30)          \r\n" + 
		" )          \r\n" + 
		"          \r\n" + 
		" INSERT INTO @regionalBR          \r\n" + 
		" EXEC usp_get_current_user_branches @user, 'slap', 1, @regions, 0, ''\r\n" + 
		"\r\n" + 
		"DECLARE @branches TABLE (Branch VARCHAR(30))          \r\n" + 
		"          \r\n" + 
		" INSERT INTO @branches          \r\n" + 
		" EXEC usp_get_current_user_branches @user, 'slap', 0, '', 1, @branch          \r\n" + 
		"\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"          \r\n" + 
		" INSERT INTO @branches          \r\n" + 
		" SELECT DISTINCT data          \r\n" + 
		" FROM dbo.Split((          \r\n" + 
		" SELECT Branches          \r\n" + 
		" FROM ATI_BEM_DW_Reports_Groups WITH (NOLOCK)          \r\n" + 
		" WHERE Group_key = @regions          \r\n" + 
		" ), ',')          \r\n" + 
		"          \r\n" + 
		" INSERT INTO @branches          \r\n" + 
		" SELECT Branch          \r\n" + 
		" FROM @regionalBR          \r\n" + 
		" WHERE region IN (          \r\n" + 
		" SELECT DISTINCT Data          \r\n" + 
		" FROM dbo.Split(@regions, ',')          \r\n" + 
		" )          \r\n" + 
		" AND Branch NOT IN (          \r\n" + 
		" SELECT Branch          \r\n" + 
		" FROM @branches          \r\n" + 
		" ) \r\n" + 
		"\r\n" + 
		" CREATE TABLE #ResultData (       \r\n" + 
		"           \r\n" + 
		"   Amount DECIMAL(18, 2)          \r\n" + 
		"  ,GBR DECIMAL(18, 2)          \r\n" + 
		"  ,GBRBPS INT      \r\n" + 
		"  ,TargetBPS INT \r\n" + 
		"  ,Variance INT\r\n" + 
		"  ,TargetRevenue DECIMAL(18, 2)         \r\n" + 
		" )      \r\n" + 
		"   \r\n" + 
		" INSERT INTO #ResultData \r\n" + 
		"\r\n" + 
		" SELECT  \r\n" + 
		"      \r\n" + 
		"  \r\n" + 
		"  sum(Slap.TotalLoanAmount) AS Amount,      \r\n" + 
		"  sum(cast(round(Slap.ActualRevenue,0) as int)) AS GBR,      \r\n" + 
		"  sum(cast(round((Slap.ActualRevenue / Slap.TotalLoanAmount) * 10000, 0) as int)) AS GBRBPS,      \r\n" + 
		"  sum(Slap.TargetBPS) as TargetBPS,\r\n" + 
		"  0 as variance,\r\n" + 
		"  round(sum(Slap.TargetRevenue),0) as TargetRevenue\r\n" + 
		"\r\n" + 
		"  --0 AS Variance     \r\n" + 
		"  \r\n" + 
		"\r\n" + 
		" FROM ATI_BEM_DW_DIM_Slap AS Slap WITH (NOLOCK)          \r\n" + 
		" WHERE Slap.Branch IN (select * from @branches)          \r\n" + 
		" AND Slap.FundedDate BETWEEN cast(@start AS DATE) AND cast(@end AS DATE)    \r\n" + 
		" AND Slap.TerminationDate IS NULL\r\n" + 
		" AND Slap.LoanOfficerName <> ''  \r\n" + 
		" AND Slap.LoanOfficerName = case when @LoanOfficer = '' then Slap.LoanOfficerName else @LoanOfficer end      \r\n" + 
		" AND Slap.LoanNumber = case when @LoanNumber = '' then Slap.LoanNumber else @LoanNumber end \r\n" + 
		"\r\n" + 
		"  \r\n" + 
		" select cast(Amount as int),\r\n" + 
		" cast(GBR as int),\r\n" + 
		" cast(round(r.GBR / r.Amount * 10000, 0) as int) as GBRBPS,\r\n" + 
		" cast(round((r.TargetRevenue / r.Amount) * 10000, 0) as int) AS  TargetBPS \r\n" + 
		" from #ResultData r    \r\n" + 
		"\r\n" + 
		" drop table #ResultData\r\n" + 
		"   \r\n" + 
		"      ";
//========================================================================================================================================================================================================================================
private String creditPulledTotal="  declare @start date = '#StartDate'            \r\n" + 
		" declare @end date = '#EndDate'        \r\n" + 
		" declare @branch varchar(2550) = #BranchList      \r\n" + 
		" declare @regions varchar(2550) = ''        \r\n" + 
		" declare @yearBy varchar(50)=''            \r\n" + 
		" declare @LoanDetail bit = 0               \r\n" + 
		" declare @LoanNumber varchar(255) = ''         \r\n" + 
		" declare @LoanOfficer varchar(255) =''         \r\n" + 
		" declare @user varchar(255) = 'ramakrishnan.s'   \r\n" + 
		"\r\n" + 
		" DECLARE @regionalBR TABLE (          \r\n" + 
		"  Branch VARCHAR(30),          \r\n" + 
		"  Region VARCHAR(30)          \r\n" + 
		" )          \r\n" + 
		"          \r\n" + 
		" INSERT INTO @regionalBR          \r\n" + 
		" EXEC usp_get_current_user_branches @user, 'slap', 1, @regions, 0, ''\r\n" + 
		"\r\n" + 
		"DECLARE @branches TABLE (Branch VARCHAR(30))          \r\n" + 
		"          \r\n" + 
		" INSERT INTO @branches          \r\n" + 
		" EXEC usp_get_current_user_branches @user, 'slap', 0, '', 1, @branch          \r\n" + 
		"\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"          \r\n" + 
		" INSERT INTO @branches          \r\n" + 
		" SELECT DISTINCT data          \r\n" + 
		" FROM dbo.Split((          \r\n" + 
		" SELECT Branches          \r\n" + 
		" FROM ATI_BEM_DW_Reports_Groups WITH (NOLOCK)          \r\n" + 
		" WHERE Group_key = @regions          \r\n" + 
		" ), ',')          \r\n" + 
		"          \r\n" + 
		" INSERT INTO @branches          \r\n" + 
		" SELECT Branch          \r\n" + 
		" FROM @regionalBR          \r\n" + 
		" WHERE region IN (          \r\n" + 
		" SELECT DISTINCT Data          \r\n" + 
		" FROM dbo.Split(@regions, ',')          \r\n" + 
		" )          \r\n" + 
		" AND Branch NOT IN (          \r\n" + 
		" SELECT Branch          \r\n" + 
		" FROM @branches          \r\n" + 
		" ) \r\n" + 
		"\r\n" + 
		" CREATE TABLE #ResultData (       \r\n" + 
		"           \r\n" + 
		"   Amount DECIMAL(18, 2)          \r\n" + 
		"  ,GBR DECIMAL(18, 2)          \r\n" + 
		"  ,GBRBPS INT      \r\n" + 
		"  ,TargetBPS INT \r\n" + 
		"  ,Variance INT\r\n" + 
		"  ,TargetRevenue DECIMAL(18, 2)         \r\n" + 
		" )      \r\n" + 
		"   \r\n" + 
		" INSERT INTO #ResultData \r\n" + 
		"\r\n" + 
		" SELECT  \r\n" + 
		"      \r\n" + 
		"  \r\n" + 
		"  sum(Slap.TotalLoanAmount) AS Amount,      \r\n" + 
		"  sum(cast(round(Slap.ActualRevenue,0) as int)) AS GBR,      \r\n" + 
		"  sum(cast(round((Slap.ActualRevenue / Slap.TotalLoanAmount) * 10000, 0) as int)) AS GBRBPS,      \r\n" + 
		"  sum(Slap.TargetBPS) as TargetBPS,\r\n" + 
		"  0 as variance,\r\n" + 
		"  round(sum(Slap.TargetRevenue),0) as TargetRevenue\r\n" + 
		"\r\n" + 
		"  --0 AS Variance     \r\n" + 
		"  \r\n" + 
		"\r\n" + 
		" FROM ATI_BEM_DW_DIM_Slap AS Slap WITH (NOLOCK)          \r\n" + 
		" WHERE Slap.Branch IN (select * from @branches)          \r\n" + 
		" AND Slap.Document_DateOrdered_CREDITREPORT BETWEEN cast(@start AS DATE) AND cast(@end AS DATE)    \r\n" + 
		" AND Slap.TerminationDate IS NULL\r\n" + 
		" AND Slap.LoanOfficerName <> ''  \r\n" + 
		" AND Slap.LoanOfficerName = case when @LoanOfficer = '' then Slap.LoanOfficerName else @LoanOfficer end      \r\n" + 
		" AND Slap.LoanNumber = case when @LoanNumber = '' then Slap.LoanNumber else @LoanNumber end \r\n" + 
		"\r\n" + 
		"  \r\n" + 
		" select cast(Amount as int),\r\n" + 
		" cast(GBR as int),\r\n" + 
		" cast(round(r.GBR / r.Amount * 10000, 0) as int) as GBRBPS,\r\n" + 
		" cast(round((r.TargetRevenue / r.Amount) * 10000, 0) as int) AS  TargetBPS \r\n" + 
		" from #ResultData r    \r\n" + 
		"\r\n" + 
		" drop table #ResultData\r\n" + 
		"   \r\n" + 
		"      ";
//========================================================================================================================================================================================================================================
String leApplicationTotal="  \r\n" + 
		"  --Code for LE Application report Total Row \r\n" + 
		"\r\n" + 
		"  declare @start date = '#StartDate'            \r\n" + 
		" declare @end date = '#EndDate'        \r\n" + 
		" declare @branch varchar(2550) = #BranchList      \r\n" + 
		" declare @regions varchar(2550) = ''        \r\n" + 
		" declare @yearBy varchar(50)=''            \r\n" + 
		" declare @LoanDetail bit = 0               \r\n" + 
		" declare @LoanNumber varchar(255) = ''         \r\n" + 
		" declare @LoanOfficer varchar(255) =''         \r\n" + 
		" declare @user varchar(255) = 'ramakrishnan.s'   \r\n" + 
		"\r\n" + 
		" DECLARE @regionalBR TABLE (          \r\n" + 
		"  Branch VARCHAR(30),          \r\n" + 
		"  Region VARCHAR(30)          \r\n" + 
		" )          \r\n" + 
		"          \r\n" + 
		" INSERT INTO @regionalBR          \r\n" + 
		" EXEC usp_get_current_user_branches @user, 'slap', 1, @regions, 0, ''\r\n" + 
		"\r\n" + 
		"DECLARE @branches TABLE (Branch VARCHAR(30))          \r\n" + 
		"          \r\n" + 
		" INSERT INTO @branches          \r\n" + 
		" EXEC usp_get_current_user_branches @user, 'slap', 0, '', 1, @branch          \r\n" + 
		"\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"          \r\n" + 
		" INSERT INTO @branches          \r\n" + 
		" SELECT DISTINCT data          \r\n" + 
		" FROM dbo.Split((          \r\n" + 
		" SELECT Branches          \r\n" + 
		" FROM ATI_BEM_DW_Reports_Groups WITH (NOLOCK)          \r\n" + 
		" WHERE Group_key = @regions          \r\n" + 
		" ), ',')          \r\n" + 
		"          \r\n" + 
		" INSERT INTO @branches          \r\n" + 
		" SELECT Branch          \r\n" + 
		" FROM @regionalBR          \r\n" + 
		" WHERE region IN (          \r\n" + 
		" SELECT DISTINCT Data          \r\n" + 
		" FROM dbo.Split(@regions, ',')          \r\n" + 
		" )          \r\n" + 
		" AND Branch NOT IN (          \r\n" + 
		" SELECT Branch          \r\n" + 
		" FROM @branches          \r\n" + 
		" ) \r\n" + 
		"\r\n" + 
		" CREATE TABLE #ResultData (       \r\n" + 
		"           \r\n" + 
		"   Amount DECIMAL(18, 2)          \r\n" + 
		"  ,GBR DECIMAL(18, 2)          \r\n" + 
		"  ,GBRBPS INT      \r\n" + 
		"  ,TargetBPS INT \r\n" + 
		"  ,Variance INT\r\n" + 
		"  ,TargetRevenue DECIMAL(18, 2)         \r\n" + 
		" )      \r\n" + 
		"   \r\n" + 
		" INSERT INTO #ResultData \r\n" + 
		"\r\n" + 
		" SELECT  \r\n" + 
		"      \r\n" + 
		"  \r\n" + 
		"  sum(Slap.TotalLoanAmount) AS Amount,      \r\n" + 
		"  sum(cast(round(Slap.ActualRevenue,0) as int)) AS GBR,      \r\n" + 
		"  sum(cast(round((Slap.ActualRevenue / Slap.TotalLoanAmount) * 10000, 0) as int)) AS GBRBPS,      \r\n" + 
		"  sum(Slap.TargetBPS) as TargetBPS,\r\n" + 
		"  0 as variance,\r\n" + 
		"  round(sum(Slap.TargetRevenue),0) as TargetRevenue\r\n" + 
		"\r\n" + 
		"  --0 AS Variance     \r\n" + 
		"  \r\n" + 
		"\r\n" + 
		" FROM ATI_BEM_DW_DIM_Slap AS Slap WITH (NOLOCK)          \r\n" + 
		" WHERE Slap.Branch IN (select * from @branches)          \r\n" + 
		" --AND Slap.Document_DateOrdered_CREDITREPORT BETWEEN cast(@start AS DATE) AND cast(@end AS DATE)\r\n" + 
		" AND Slap.ApplicationDate BETWEEN cast(@start AS DATE) AND cast(@end AS DATE)    \r\n" + 
		" --AND Slap.FundedDate BETWEEN cast(@start AS DATE) AND cast(@end AS DATE)    \r\n" + 
		" AND Slap.TerminationDate IS NULL\r\n" + 
		" AND Slap.LoanOfficerName <> ''  \r\n" + 
		" AND Slap.LoanOfficerName = case when @LoanOfficer = '' then Slap.LoanOfficerName else @LoanOfficer end      \r\n" + 
		" AND Slap.LoanNumber = case when @LoanNumber = '' then Slap.LoanNumber else @LoanNumber end \r\n" + 
		"\r\n" + 
		"  \r\n" + 
		" select cast(Amount as int),\r\n" + 
		" cast(GBR as int),\r\n" + 
		" cast(round(r.GBR / r.Amount * 10000, 0) as int) as GBRBPS,\r\n" + 
		" cast(round((r.TargetRevenue / r.Amount) * 10000, 0) as int) AS  TargetBPS \r\n" + 
		" from #ResultData r    \r\n" + 
		"\r\n" + 
		" drop table #ResultData\r\n" + 
		"   \r\n";  
		
//========================================================================================================================================================================================================================================
String locksTotal="  \r\n" + 
		"  --Code for LE Application report Total Row \r\n" + 
		"\r\n" + 
		"  declare @start date = '#StartDate'            \r\n" + 
		" declare @end date = '#EndDate'        \r\n" + 
		" declare @branch varchar(2550) = #BranchList      \r\n" + 
		" declare @regions varchar(2550) = ''        \r\n" + 
		" declare @yearBy varchar(50)=''            \r\n" + 
		" declare @LoanDetail bit = 0               \r\n" + 
		" declare @LoanNumber varchar(255) = ''         \r\n" + 
		" declare @LoanOfficer varchar(255) =''         \r\n" + 
		" declare @user varchar(255) = 'ramakrishnan.s'   \r\n" + 
		"\r\n" + 
		" DECLARE @regionalBR TABLE (          \r\n" + 
		"  Branch VARCHAR(30),          \r\n" + 
		"  Region VARCHAR(30)          \r\n" + 
		" )          \r\n" + 
		"          \r\n" + 
		" INSERT INTO @regionalBR          \r\n" + 
		" EXEC usp_get_current_user_branches @user, 'slap', 1, @regions, 0, ''\r\n" + 
		"\r\n" + 
		"DECLARE @branches TABLE (Branch VARCHAR(30))          \r\n" + 
		"          \r\n" + 
		" INSERT INTO @branches          \r\n" + 
		" EXEC usp_get_current_user_branches @user, 'slap', 0, '', 1, @branch          \r\n" + 
		"\r\n" + 
		"\r\n" + 
		"\r\n" + 
		"          \r\n" + 
		" INSERT INTO @branches          \r\n" + 
		" SELECT DISTINCT data          \r\n" + 
		" FROM dbo.Split((          \r\n" + 
		" SELECT Branches          \r\n" + 
		" FROM ATI_BEM_DW_Reports_Groups WITH (NOLOCK)          \r\n" + 
		" WHERE Group_key = @regions          \r\n" + 
		" ), ',')          \r\n" + 
		"          \r\n" + 
		" INSERT INTO @branches          \r\n" + 
		" SELECT Branch          \r\n" + 
		" FROM @regionalBR          \r\n" + 
		" WHERE region IN (          \r\n" + 
		" SELECT DISTINCT Data          \r\n" + 
		" FROM dbo.Split(@regions, ',')          \r\n" + 
		" )          \r\n" + 
		" AND Branch NOT IN (          \r\n" + 
		" SELECT Branch          \r\n" + 
		" FROM @branches          \r\n" + 
		" ) \r\n" + 
		"\r\n" + 
		" CREATE TABLE #ResultData (       \r\n" + 
		"           \r\n" + 
		"   Amount DECIMAL(18, 2)          \r\n" + 
		"  ,GBR DECIMAL(18, 2)          \r\n" + 
		"  ,GBRBPS INT      \r\n" + 
		"  ,TargetBPS INT \r\n" + 
		"  ,Variance INT\r\n" + 
		"  ,TargetRevenue DECIMAL(18, 2)         \r\n" + 
		" )      \r\n" + 
		"   \r\n" + 
		" INSERT INTO #ResultData \r\n" + 
		"\r\n" + 
		" SELECT  \r\n" + 
		"      \r\n" + 
		"  \r\n" + 
		"  sum(Slap.TotalLoanAmount) AS Amount,      \r\n" + 
		"  sum(cast(round(Slap.ActualRevenue,0) as int)) AS GBR,      \r\n" + 
		"  sum(cast(round((Slap.ActualRevenue / Slap.TotalLoanAmount) * 10000, 0) as int)) AS GBRBPS,      \r\n" + 
		"  sum(Slap.TargetBPS) as TargetBPS,\r\n" + 
		"  0 as variance,\r\n" + 
		"  round(sum(Slap.TargetRevenue),0) as TargetRevenue\r\n" + 
		"\r\n" + 
		"  --0 AS Variance     \r\n" + 
		"  \r\n" + 
		"\r\n" + 
		" FROM ATI_BEM_DW_DIM_Slap AS Slap WITH (NOLOCK)          \r\n" + 
		" WHERE Slap.Branch IN (select * from @branches)          \r\n" + 
		" --AND Slap.Document_DateOrdered_CREDITREPORT BETWEEN cast(@start AS DATE) AND cast(@end AS DATE)\r\n" + 
		" --AND Slap.ApplicationDate BETWEEN cast(@start AS DATE) AND cast(@end AS DATE)    \r\n" + 
		" AND Slap.LockDate BETWEEN cast(@start AS DATE) AND cast(@end AS DATE)    \r\n" + 
		" --AND Slap.FundedDate BETWEEN cast(@start AS DATE) AND cast(@end AS DATE)    \r\n" + 
		" AND Slap.TerminationDate IS NULL\r\n" + 
		" AND Slap.LoanOfficerName <> ''  \r\n" + 
		" AND Slap.LoanOfficerName = case when @LoanOfficer = '' then Slap.LoanOfficerName else @LoanOfficer end      \r\n" + 
		" AND Slap.LoanNumber = case when @LoanNumber = '' then Slap.LoanNumber else @LoanNumber end \r\n" + 
		"\r\n" + 
		"  \r\n" + 
		" select cast(Amount as int),\r\n" + 
		" cast(GBR as int),\r\n" + 
		" cast(round(r.GBR / r.Amount * 10000, 0) as int) as GBRBPS,\r\n" + 
		" cast(round((r.TargetRevenue / r.Amount) * 10000, 0) as int) AS  TargetBPS \r\n" + 
		" from #ResultData r    \r\n" + 
		"\r\n" + 
		" drop table #ResultData\r\n" + 
		"   \r\n";

String TotalBeginningYearBalance ="declare @branch varchar(200)= #RegionNumbers\r\n" + 
		"declare @tempSOYB table (Branch varchar(50), Amount decimal(18,2))            \r\n" + 
		"insert into @tempSOYB   \r\n" + 
		"\r\n" + 
		"SELECT Branch_Key as Branch, sum(Amount)*-1 as Amount\r\n" + 
		"FROM [acct].[ati_bem_dw_Fact_GeneralLedger] \r\n" + 
		"where Account_Key = '91110' and year(JournalPostDate) = year(GETDATE())-1 and Branch_Key in (select data from dbo.Split(@Branch, ',') )\r\n" + 
		"group by Branch_Key            \r\n" + 
		"            \r\n" + 
		"--NET INCOME (LOSS) FOR JAN-2017            \r\n" + 
		"declare @tempNIL table (Branch varchar(50), Amount decimal(18,2))    \r\n" + 
		"insert into @tempNIL            \r\n" + 
		"SELECT Branch_Key as Branch, sum(Amount)*-1 as Amount            \r\n" + 
		"FROM [acct].[ati_bem_dw_Fact_GeneralLedgerLoanDetails]    \r\n" + 
		"where Account_Key in (select distinct Account_Key from Acct.ATI_BEM_DW_DIM_AccountTree at            \r\n" + 
		"     inner join Acct.ATI_BEM_DW_DIM_AccountTreeAccountResolution atr on at.ATKey = atr.ATKey            \r\n" + 
		"     where at.NodeName = 'Net Income (Loss)') \r\n" + 
		"and year(JournalPostDate) = year(GETDATE())-1  \r\n" + 
		"and Branch_Key in (select data from dbo.Split(@Branch, ',') )\r\n" + 
		"group by Branch_Key            \r\n" + 
		"            \r\n" + 
		"declare @tempSOYBNewYear table (Branch varchar(50), Amount decimal(18,2))            \r\n" + 
		"            \r\n" + 
		"--CALCULATE NEW YEAR START OF YEAR BALANCE            \r\n" + 
		"--insert into @tempSOYBNewYear            \r\n" + 
		"select  sum(            \r\n" + 
		"isnull(soyb.Amount, 0) + isnull(nil.Amount, 0))             \r\n" + 
		"from @tempSOYB soyb            \r\n" + 
		"full join @tempNIL nil on nil.Branch = soyb.Branch ";

//========================================================================================================================================================================================================================================
	/*public String getSlap_Dashboard_Grid_LoanCount_FccDB()
	{
		return slap_Dashboard_Grid_LoanCount_FccDB;
	}*/
	public String getSlap_Dashboard_Grid_LoanCountQuery()
	{
		return slap_Dashboard_Grid_LoanCount;
	}
	String getQueryByAttributeName(String dataMemberName) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		String query = null;
		try {
			Field f=this.getClass().getDeclaredField(dataMemberName);
			query=f.get(this).toString();
		}
		catch(Exception e)
		{
			System.out.println(" \n\nQuery for "+dataMemberName+" doestn't exist. Plesase declare or check the variable in FccQuery\n\n");
		}
	
	return query;	
	}

}
