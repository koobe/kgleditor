<html>
	<head>
		<meta name="kgl:media_count" content="3"/> 
		<meta name="kgl:text_count" content="1" /> 
	   <link rel="stylesheet" href="/assets/bootstrap.css?compile=false"  />
	    <title>Content template: three_picture_horizontal_title_first</title>
	    <style type="text/css">
	    	.template-container {
	    		width:100%;
	    		height:100%;
	    	}
	    	
	    	.pictures-container{
	    		display: table;
	    		width: 100%;
	    		height: 60%;
	    		border-spacing: 20px;
	    	}
	    	
	    	.picture-item {
	    		display: table-cell;
	    		border-radius: 5px;
	    		background-position: center; background-repeat: no-repeat; background-size: cover;
	    	}
	    	
	    	.text-container {
	    		padding-left: 20px;
	    		padding-right: 20px;
	    	}
	    	
	    	.content-author {
	    		border-bottom: 1px solid #a9c6e6;
	    	}
	    	
	    	.content-text {
	    		margin-top: 10px;
	    	}
	    	
	    	p {
	    		font-size: 17px;
	    		text-align:justify;
	    	}
	    </style>
	</head>
	<body>
		<div class="template-container">
			<div class="text-container">
				<div class="content-title">
					<h2>${content.cropTitle}</h2>
				</div>
				<div class="content-author">
					<h4>${content.user.fullName}</h4>
				</div>
			</div>
			<div class="pictures-container">
				<div class="picture-item" style="background-image:url(${content.pictureSegments[0].originalUrl});"></div>
				<div class="picture-item" style="background-image:url(${content.pictureSegments[1].originalUrl});"></div>
				<div class="picture-item" style="background-image:url(${content.pictureSegments[2].originalUrl});"></div>
			</div>
			<div class="text-container">
				
				<div class="content-text">
					<g:each in="${content.textSegments}" var="segment">
						<p>${segment.text}</p>
					</g:each>
				</div>
			</div>
		</div>
	</body>
</html>