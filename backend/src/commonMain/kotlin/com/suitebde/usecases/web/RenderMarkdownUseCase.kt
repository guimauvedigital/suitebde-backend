package com.suitebde.usecases.web

class RenderMarkdownUseCase : IRenderMarkdownUseCase {

    private val rules = listOf(
        // Headers
        Regex("""^#{6}\s?([^\n]+)""", RegexOption.MULTILINE) to "<h6>$1</h6>",
        Regex("""^#{5}\s?([^\n]+)""", RegexOption.MULTILINE) to "<h5>$1</h5>",
        Regex("""^#{4}\s?([^\n]+)""", RegexOption.MULTILINE) to "<h4>$1</h4>",
        Regex("""^#{3}\s?([^\n]+)""", RegexOption.MULTILINE) to "<h3>$1</h3>",
        Regex("""^#{2}\s?([^\n]+)""", RegexOption.MULTILINE) to "<h2>$1</h2>",
        Regex("""^#{1}\s?([^\n]+)""", RegexOption.MULTILINE) to "<h1>$1</h1>",

        // Bold, italic
        Regex("""\*\*\s?([^\n]+)\*\*""") to "<strong>$1</strong>",
        Regex("""\*\s?([^\n]+)\*""") to "<em>$1</em>",

        // Images
        Regex("""!\[([^\]]+)\]\(([^)]+)\)""")
                to "<img class=\"img-fluid rounded mx-auto d-block\" src=\"$2\" alt=\"$1\" />",

        // Links
        Regex("""\[([^\]]+)\]\(([^)]+)\)""")
                to "<a href=\"$2\">$1</a>",

        // Lists
        Regex("""^\+([^\n]+)""", RegexOption.MULTILINE) to "<ul><li>$1</li></ul>",
        Regex("""^\*([^\n]+)""", RegexOption.MULTILINE) to "<ul><li>$1</li></ul>",
        Regex("""^\-([^\n]+)""", RegexOption.MULTILINE) to "<ul><li>$1</li></ul>",

        // Containers
        Regex("""\{\s*\{\s*([^\}]+)\s*\}\s*\{\s*([^\}]+)\s*\}\s*\}""")
                to "<div class=\"grid grid-cols-1 md:grid-cols-2 gap-7.5\"><div>\n$1\n</div><div>\n$2\n</div></div>",
        Regex("""([^\n]+\n?)""") to "<p class=\"pb-7.5\">$1</p>",
    )

    override fun invoke(input: String): String = rules.fold(input) { acc, pair ->
        acc.replace(pair.first, pair.second)
    }

}
