# Inline Calc

An IntelliJ Platform plugin that evaluates the text you select in the editor as a math expression and shows the result in the status bar.

Select `(1 + 2) * 3` and the status bar shows `= 9`. Click the result to copy it to the clipboard.

## Features

- Evaluates the current selection with [EvalEx](https://github.com/ezylang/EvalEx), using arbitrary-precision decimals (`0.1 + 0.2` is `0.3`).
- Supports the usual operators (`+ - * / % ^`), parentheses and the functions and constants EvalEx provides.
- Click the status bar widget, or use the **Copy Expression Result** action, to copy the plain result.
- Selections that are not valid numeric expressions are ignored silently.

The **Copy Expression Result** action has no default shortcut. Assign one in *Settings | Keymap*.

## Settings

*Settings | Tools | Inline Calc*

| Setting | Default | Description |
|---|---|---|
| Enable expression evaluation | on | Turns the widget on or off. |
| Digit grouping | None | Separator for thousands in the displayed result: none, space, comma, period or apostrophe. The copied value is never grouped. |
| Result prefix | `= ` | Text shown before the result. |
| Show expression with result | off | Shows the evaluated expression before the result. |
| Maximum expression length | 300 | Longer selections are not evaluated. |
| Ignored characters | none | Characters removed from the selection before evaluation (space, comma, apostrophe, underscore), so that `1'000 + 2_000` can be evaluated. |

## Development

Requires JDK 21.

```sh
./gradlew runIde        # start a sandbox IDE with the plugin
./gradlew check         # run tests
./gradlew verifyPlugin  # run the IntelliJ Plugin Verifier
./gradlew buildPlugin   # build the distribution ZIP in build/distributions
```

Run configurations for these tasks are in `.run/`.

## Releasing

Pushes to `main` build and test the plugin and prepare a draft GitHub release from the `Unreleased` section of [CHANGELOG.md](CHANGELOG.md). Publishing that release triggers publishing to JetBrains Marketplace and a pull request that patches the changelog. See `.github/workflows/`.

## License

Licensed under the [Apache License, Version 2.0](LICENSE). Third-party notices are in [NOTICE](NOTICE).
