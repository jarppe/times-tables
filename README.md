# Times Tables

Simple [ClojureScript](https://github.com/clojure/clojurescript) app to visualize 
the Times Tables. The running app can be found from [here](http://todo). 

## What is this?

I watched the really interesting [Times Tables video](https://www.youtube.com/watch?v=qhbuKbxJsk8) 
by [Mathologer](https://www.youtube.com/channel/UC1_uAIS3r8Vu6JjXWvastJg) and I decided
to write some ClojureScriptcode to play with the idea. This is the result.

The code here implements the algorithim presented in the video. It allows users to interactively
adjust the number of `dots` and the `times` values. The result is drawn on HTML5 canvas.

The application is tested to work with recent Google Chrome, Safari and Firefox browsers.

## Development Mode

This application is written on ClojureScript and it uses Reagent and Figwheel.

Start development setup:
 
```
lein dev
```

This starts Less compiler and ClojureScript compiler. When you get the ClojureScript REPL prompt,
the app is running. Open your browser to [http://localhost:3449](http://localhost:3449) and you 
can play with the app. Changes to sources are pushed automatically to browser.

## Production Build

```
lein dist
```

The distributable app is built to `./dist` folder.

## License

Copyright &copy; 2016 Jarppe

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

For more information, see the `LICENSE` file.
