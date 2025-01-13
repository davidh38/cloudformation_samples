import emoji
import sys
print(sys.path)
# Basic emoji usage
print("Basic emojis:")
print(emoji.emojize("Python is :thumbs_up:"))
print(emoji.emojize("I love coding :red_heart:"))

# Multiple emojis in a sentence
print("\nA day in the life:")
print(emoji.emojize("Wake up :sun_with_face: Drink coffee :hot_beverage: Code :laptop: Sleep :sleeping_face:"))

# Using aliases
print("\nUsing aliases:")
print(emoji.emojize("Coding is :computer: and it's :fire:!", language='alias'))

# Get a random emoji
print("\nRandom emoji:")
print(emoji.emojize(":star-struck:"))
