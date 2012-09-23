<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0//EN">

<html>
	<head>
		<style>
			body {
				background-color: #eef0f6;
				margin: 0px;
				font-size: .9em;
			}
			
			a, a:hover, a:visited { color:black; text-decoration:none; }
			a, a:visited { border-bottom: 1px dotted black; }
			a:hover { border-bottom: 1px solid black; }
				
			#content { 
				background-color: white; 
				margin: 0px 250px 0px 20px;
				padding: 20px;
				border-top: 1px solid #6A829E;
				border-left: 1px solid #6A829E;
				border-right: 1px solid #6A829E;
				border-bottom: 1px solid #6A829E;
				height: 800px;
			}
			h2.title { margin: 0px; padding: 20px;}
			
			#sidebar {
				position: absolute;
				right: 10px; top: 60px;
				width: 200px;
				padding: 0 10px 10px 10px;				
			}
			#sidebar h3 { margin: 10px 0px 5px 0px; }
			#sidebar p { margin: 10px 5px 10px 5px;}
			#sidebar li { list-style: none; margin: 0px; padding: 5px 0px 5px 0px;}
			#sidebar ul { margin: 5px; padding: 0px;}			
			#sidebar li { padding: 5px 0px 5px 0px;}			
			#sidebar table { width: 160px; margin: 5px;}			
			#sidebar td { border-bottom: 1px solid #6A829E; padding: 2px 5px 2px 5px;}
			#sidebar .col2 { text-align: right; }
		</style>
	</head>
<body>
	<div id="sidebar">
		<p>
		For tips, on how to build and deploy this application, please see the <a href="http://wiki.stax.net/w/index.php/GettingStarted/Simple">tutorial</a>.
		</p>
		<h3>Stax links</h3>
		<ul>
			<li><a href="http://www.stax.net/appconsole">Stax Application Console</a></li>
			<li><a href="http://wiki.stax.net/w/index.php/Main_Page">Stax Developer Wiki</a></li>
		</ul>		
		<h3>Runtime information</h3>
		<table cellpadding="0" cellspacing="0">
			<tr><td class="col1">Environment:</td><td class="col2"><%=pageContext.getServletContext().getInitParameter("application.environment") %></td></tr>
			<tr><td class="col1">Environment2:</td><td class="col2"><%=pageContext.getServletContext().getInitParameter("application.environment2") %></td></tr>
			<tr><td class="col1">Server:</td><td class="col2"><%= java.net.InetAddress.getLocalHost().getHostName() %></td></tr>
		</table>
		<p><a href="/events">Click here to reload</a></p>
	</div>
	<div id="content">
		<h3>Events list</h3>
                <table>
                    <thead>
                        <tr>
                           <td>ID</td>
                           <td>Data key</td>
                           <td colspan="6">Values</td>
                        </tr>
                    </thead>
                    <tbody>
                <% java.util.List rows = (java.util.List) request.getAttribute("rows");
                   for (int index = 0; index < rows.size(); index++) { 
                       com.plugtree.bi.api.Row row = (com.plugtree.bi.api.Row) rows.get(index); %>
                       <tr>
                           <td><%=row.getId()%></td>
                           <td><%=row.getKey()%></td>
                           <td><%=row.getValue1()%></td>
                           <td><%=row.getValue2()%></td>
                           <td><%=row.getValue3()%></td>
                           <td><%=row.getValue4()%></td>
                           <td><%=row.getValue5()%></td>
                           <td><%=row.getValue6()%></td>
                       </tr>
                <% } %></tbody>
                </table>
	</div>
</body>
</html>
