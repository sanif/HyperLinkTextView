
# HyperLinkTextView
Customizable hyperlink TextView for Android.  
  
  


## How to use:
Step1: Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}   
  


    
Step 2. Add the dependency  
[![GitHub release](https://img.shields.io/github/release/sanif/HyperLinkTextView.svg)](https://github.com/sanif/HyperLinkTextView/releases/latest)

Note: replace Tag with latest version

	dependencies {
	        implementation 'com.github.sanif:HyperLinkTextView:Tag'
	}


## Following properties can be customized:
```
<resources>
    <declare-styleable name="hyperlink_text_view_attrs">
        <attr name="linkType" format="enum">
            <enum name="web" value="0"/>
            <enum name="email" value="1"/>
            <enum name="telephone" value="2"/>
            <enum name="copy" value="3"/>
        </attr>

        <attr name="linkColor" format="color"/>
        <attr name="underlined" format="boolean"/>
        <attr name="clickBehaviour" format="enum">
            <enum name="click" value="0"/>
            <enum name="longPress" value="1"/>
        </attr>
        <attr name="linkStartOffset" format="integer"/>
        <attr name="linkEndOffset" format="integer"/>
    </declare-styleable>
</resources>
```

## You can set text dynamically
`textView.setCustomText("http://google.com")`
