import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.coref.data.Mention;
import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;


public class StanfordParserDemo {
    public static List<String> tokenize(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        List<CoreLabel> tokens = document.get(TokensAnnotation.class);

        List<String> result = new ArrayList<String>();
        for (CoreLabel token : tokens) {
            // this is the text of the token
            String word = token.get(TextAnnotation.class);
            result.add(word);
        }

        return result;
    }

    public static List<String> sentenceSplit(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);

        List<String> result = new ArrayList<String>();
        for (CoreMap sentence : sentences) {
            String sentenceString = sentence.get(TextAnnotation.class);
            result.add(sentenceString);

            // see tokenize(String) method
            List<CoreLabel> tokens = sentence.get(TokensAnnotation.class);
            for (CoreLabel token : tokens) {
                String word = token.get(TextAnnotation.class);
            }
        }

        return result;
    }

    public static List<String> posTagging(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        List<CoreLabel> tokens = document.get(TokensAnnotation.class);

        List<String> result = new ArrayList<String>();
        for (CoreLabel token : tokens) {
            // this is the text of the token
            String pos = token.get(PartOfSpeechAnnotation.class);
            result.add(pos);
        }

        return result;
    }

    public static List<String> lemmatize(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        List<CoreLabel> tokens = document.get(TokensAnnotation.class);

        List<String> result = new ArrayList<String>();
        for (CoreLabel token : tokens) {
            // this is the text of the token
            String lemma = token.get(LemmaAnnotation.class);
            result.add(lemma);
        }

        return result;
    }

    public static List<String> ner(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        List<CoreLabel> tokens = document.get(TokensAnnotation.class);

        List<String> result = new ArrayList<String>();
        for (CoreLabel token : tokens) {
            // this is the text of the token
            String nerTag = token.get(NamedEntityTagAnnotation.class);
            result.add(nerTag);
        }

        return result;
    }

    public static List<Tree> parse(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);

        List<Tree> result = new ArrayList<Tree>();
        for (CoreMap sentence : sentences) {
            Tree tree = sentence.get(TreeAnnotation.class);
            result.add(tree);
        }

        return result;
    }

    public static Map<Integer,CorefChain> coreferenceResolution(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, mention,coref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);

        return document.get(CorefCoreAnnotations.CorefChainAnnotation.class);
    }

    public static HashMap<String,List<String>> coreferenceMentions(String text) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, mention,coref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);


        HashMap<String,List<String>> resultBySentence = new HashMap<String,List<String>>();
        for (CoreMap sentence : document.get(SentencesAnnotation.class)) {
            List<String> result = resultBySentence.get(sentence);
            if(result == null) {
                result = new ArrayList<String>();
            }

            for (Mention m : sentence.get(CorefCoreAnnotations.CorefMentionsAnnotation.class)) {
                result.add(m.toString());
            }

            resultBySentence.put(sentence.toString(),result);
        }

        return resultBySentence;
    }

    public static void main(String[] args) {
        String text =
                "Pierre Vinken, 61 years old, will join the board as a nonexecutive director Nov. 29. "
                        + "Mr. Vinken is chairman of Elsevier N.V., the Dutch publishing group. "
                        + "Rudolph Agnew, 55 years old and former chairman of Consolidated Gold Fields PLC, "
                        + "was named a director of this British industrial conglomerate.";

        List<String> tokens = tokenize(text);
        for (String token : tokens) {
            System.out.print("[" + token + "] ");
        }
        System.out.println();
        System.out.println("=====================================================");

        List<String> sentences = sentenceSplit(text);
        for (String sentence : sentences) {
            System.out.print("[" + sentence + "] ");
        }
        System.out.println();
        System.out.println("=====================================================");

        List<String> posTags = posTagging(text);

        for(int index = 0; index < tokens.size(); index++) {
            String token = tokens.get(index);
            String posTag = posTags.get(index);

            System.out.print("[" + token + "/" + posTag + "] ");
        }
        System.out.println();
        System.out.println("=====================================================");

        List<String> lemmas = lemmatize(text);
        for (String lemma : lemmas) {
            System.out.print("[" + lemma + "] ");
        }
        System.out.println();
        System.out.println("=====================================================");

        List<String> nerTags = ner(text);
        for(int index = 0; index < tokens.size(); index++) {
            String token = tokens.get(index);
            String nerTag = nerTags.get(index);

            System.out.print("[" + token + "/" + nerTag + "] ");
        }
        System.out.println();
        System.out.println("=====================================================");

        List<Tree> trees = parse(text);
        for (Tree tree : trees) {
            String strTree = tree.toString();
            strTree = strTree.replace('(', '[');
            strTree = strTree.replace(')', ']');
            System.out.println(strTree);
        }
        System.out.println();
        System.out.println("=====================================================");

        text = "Victoria Chen, Chief Financial Officer of Megabucks Banking Corp since 2004, saw her pay jump 20%," +
                " to $1.3 million, as the 37-year-old also became the Denver-based financial-services company 92s" +
                " president. It has been ten years since she came to Megabucks from rival Lotsabucks.";
        Map<Integer,CorefChain> graph = coreferenceResolution(text);
        for (CorefChain entry : graph.values()) {
            System.out.println(entry);
        }
        System.out.println();
        System.out.println("=====================================================");

        HashMap<String,List<String>> mentionsBySentence = coreferenceMentions(text);
        for (String sentence : mentionsBySentence.keySet()) {
            System.out.println("Mentions for [" + sentence + "]");
            for (String m : mentionsBySentence.get(sentence)) {
                System.out.print("[" + m + "] ");
            }
        }
        System.out.println();
        System.out.println("=====================================================");
    }
}
