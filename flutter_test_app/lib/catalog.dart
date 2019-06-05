import 'package:flutter/material.dart';
import 'package:flutter_test_app/cart.dart';
import 'package:flutter_test_app/main.dart';
import 'package:scoped_model/scoped_model.dart';

class Catalog extends StatefulWidget {
  @override
  CatalogState createState() => new CatalogState();
}

class Product {
  String name;
  double price;

  Product(String name, double price) {
    this.name = name;
    this.price = price;
  }
}

class CatalogState extends State<Catalog> {
  List<Product> _products = [
    Product("WII", 123.00),
    Product("IPhone X", 1337.12),
    Product("Zakdoek", 12),
    Product("Gucci belt", 400)
  ];
  final _biggerFont = const TextStyle(fontSize: 18.0);

  @override
  Widget build(BuildContext context) {
    final _cart = ScopedModel.of<CartModel>(context, rebuildOnChange: true);

    return new Scaffold(
        appBar: new AppBar(
          title: new Text('Startup Name Generator'),
          actions: <Widget>[
            new IconButton(
                icon: const Icon(Icons.shopping_cart), onPressed: _pushSaved),
          ],
        ),
        body: _buildSuggestions(_cart));
  }

  Widget _buildSuggestions(CartModel _cart) {
    return ListView.builder(
        padding: const EdgeInsets.all(16.0),
        itemCount: _products.length,
        itemBuilder: (context, index) {
          return _buildRow(_cart, _products[index]);
        });
  }

  Widget _buildRow(CartModel _cart, Product product) {
    final bool itemInCart = _cart.items.contains(product); // Add this line.
    return new ListTile(
      title: new Text(
        product.name,
        style: _biggerFont,
      ),
      trailing: new Icon(
        itemInCart ? Icons.check : Icons.add,
        color: null,
      ),
      onTap: () {
        setState(() {
          if (itemInCart) {
            _cart.remove(product);
          } else {
            _cart.add(product);
          }
        });
      },
    );
  }

  void _pushSaved() {
    Navigator.of(context).push(
      new MaterialPageRoute<void>(
        builder: (BuildContext context) {
          return new Cart();
        },
      ),
    );
  }
}
