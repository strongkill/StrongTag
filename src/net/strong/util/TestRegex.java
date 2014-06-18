package net.strong.util;

import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Perl5Substitution;
import org.apache.oro.text.regex.Util;

public class TestRegex {
    public TestRegex() {}
    public void parseLog() throws Exception {
        String log1="172.26.155.241 - - [26/Feb/2001:10:56:03 -0500] \"GET /IsAlive.htm HTTP/1.0\" 200 15 ";

        String regexp="(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\s-\\s-\\s\\[([^\\]]+)\\]";

        PatternCompiler compiler=new Perl5Compiler();
        Pattern pattern=compiler.compile(regexp);

        PatternMatcher matcher=new Perl5Matcher();
        if (matcher.contains(log1,pattern)) {
            MatchResult result=matcher.getMatch();
            System.out.println("IP: "+result.group(1));
            System.out.println("Timestamp: "+result.group(2));
        }
    }
    public void parseHTML() throws Exception {
        String html="<font face=\"Arial, Serif\" size=\"+1\"color=\"red\">";

        String regexpForFontTag="<\\s*font\\s+([^>]*)\\s*>";
        String regexpForFontAttrib="([a-z]+)\\s*=\\s*\"([^\"]+)\"";

        PatternCompiler compiler=new Perl5Compiler();
        Pattern patternForFontTag=compiler.compile(regexpForFontTag,Perl5Compiler.CASE_INSENSITIVE_MASK);
        Pattern patternForFontAttrib=compiler.compile(regexpForFontAttrib,Perl5Compiler.CASE_INSENSITIVE_MASK);

        PatternMatcher matcher=new Perl5Matcher();
        if (matcher.contains(html,patternForFontTag)) {
            MatchResult result=matcher.getMatch();
            String attrib=result.group(1);

            PatternMatcherInput input=new PatternMatcherInput(attrib);
            while (matcher.contains(input,patternForFontAttrib)) {
                result=matcher.getMatch();
                System.out.println(result.group(1)+": "+result.group(2));
            }
        }
    }
    public void substitutelink() throws Exception {
        String link="<a href=\"http://widgets.acme.com/interface.html#How_To_Trade\">";
        String regexpForLink="<\\s*a\\s+href\\s*=\\s*\"http://widgets.acme.com/interface.html#([^\"]+)\">";

        PatternCompiler compiler=new Perl5Compiler();
        Pattern patternForLink=compiler.compile(regexpForLink,Perl5Compiler.CASE_INSENSITIVE_MASK);

        PatternMatcher matcher=new Perl5Matcher();

        String result=Util.substitute(matcher,
                                      patternForLink,
                                      new Perl5Substitution("<a href=\"http://newserver.acme.com/interface.html#$1\">"),
                                      link,
                                      Util.SUBSTITUTE_ALL);

        System.out.println(result);
    }
    public static void main(String[] args) throws Exception {
        TestRegex test=new TestRegex();
        System.out.println("\n\nLog Parsing Example");
        test.parseLog();
        System.out.println("\n\nHtml Example 1");
        test.parseHTML();
        System.out.println("\n\nHtml Example 2");
        test.substitutelink();
	}
}

