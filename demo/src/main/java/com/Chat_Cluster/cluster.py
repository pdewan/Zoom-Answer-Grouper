import os
from openai import OpenAI

def main():
    client = OpenAI()
    client.api_key = os.environ.get("OPENAI_API_KEY_DEWAN")
    messages = [ {"role": "system", "content": "You are a intelligent assistant."} ]
    message = ""
    write = "data/Zoom-Chats/2023-09-19 12.55.01 Comp 524 Lectures Fall 2023/prompts/2.txt"
    response = "data/Zoom-Chats/2023-09-19 12.55.01 Comp 524 Lectures Fall 2023/outputs/2.txt"
    with open(write, "r") as prompt:
        message = prompt.read()


    messages.append(
        {"role": "user", "content": message},
    )
    # print(messages)
    chat = client.chat.completions.create(
    model="gpt-4-1106-preview",
    messages=messages,
    )
    reply = chat.choices[0].message.content
    with open(response, "w") as output_file:
        output_file.write(reply)

if __name__ == "__main__":
    main()